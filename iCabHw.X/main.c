#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Config.h"
#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"
#include "Drivers/TMR0_Driver.h"
#include "Drivers/TMR1_Driver.h"
#include "Drivers/PWM_Driver.h"
#include "Controllers/DOOR_Controller.h"

#define LED1    PORTBbits.RB5

#define COMMAND_LOCK    "L" /* Command to lock doors                          */
#define COMMAND_UNLOCK  "U" /* Command to unlock doors                        */
#define COMMAND_INIT    "I" /* Command to initialize                          */
#define COMMAND_RESET   "R" /* Command to reset PIC                           */
#define COMMAND_PING    "P" /* Command to ping PIC                            */
#define COMMAND_ERROR   "E" /* Command to indicate error                      */
#define COMMAND_ALARM   "A" /* Command to set alarm                           */
#define COMMAND_STATE   "S" /* Command to send state of PIC                   */

#define ERROR_UNKNOWN   "U" /* Error message to send when unknown command     */

#define ALARM_OFF     0
#define ALARM_SOFT    1
#define ALARM_HARD    2
#define ALARM_COMM    3

uint8_t lockDelayCnt;
uint8_t communicationCnt;
uint8_t ledDelayCnt;
READ_Data read;
bool tick;
bool lock;
bool running;

uint8_t pwm;
uint8_t newAlarm;
uint8_t oldAlarm;

void run();
void initDoors(uint8_t door_cnt);
void setAlarm(uint8_t alarm);

void initDoors(uint8_t door_cnt) {
    if (door_cnt < 1) {
        door_cnt = 1;
    }
    // Doors
    C_DOOR_Init(door_cnt);
    C_DOOR_LockAll();
    
    // Start timer
    D_TMR0_Enable(true);
}

void setAlarm(uint8_t alarm) {
    newAlarm = alarm;
}

void main(void) {
    __delay_ms(200);
    // Initialize ports to defaults
    D_PORT_Init();
    
    // Initialize the UART module with a baud rate of SERIAL_BAUD, with the use 
    // of interrupts.
    D_UART_Init("P", SERIAL_BAUD, true);
    D_UART_Enable(true);
    
    // Initialize timers
    D_TMR0_Init();
    D_TMR1_Init();
    
    // Initialize PWM
    D_PWM_Init();
    
    __delay_ms(200);
    
    newAlarm = 0;
    oldAlarm = 0;
    lock = false;
    tick = false;
    running = false;
    
    __delay_ms(200);
    if (LOCK) {
        PORTB = 0x00;
    } else {
        PORTB = 0xFF;
    }
    
    run();
//    TRISAbits.TRISA1 = 1;
//    TRISAbits.TRISA2 = 1;
//    TRISAbits.TRISA3 = 1;
//    TRISAbits.TRISA4 = 1;
//    TRISAbits.TRISA5 = 1;
//    while(1) {
//        LED1 = PORTAbits.RA5;
//    }
}

void run() {
    while(1) {
        
        // Close doors
        if (lock) {
            D_TMR1_Enable(false);
            lockDelayCnt = 0;
            C_DOOR_LockAll();
            lock = false;
        }
        
        // Serial
        if (readReady) {
            readReady = false;
            read = D_UART_Read();
            
            
            // Acknowledge
            D_UART_Acknowledge(read.ackId);
            
            // Communication
            communicationCnt = 0;
            if (oldAlarm == ALARM_COMM) {
                newAlarm = ALARM_OFF;
            } 
            
            // Lock doors: start timer 
            if (strcmp(read.command, COMMAND_LOCK) == 0) {
                lockDelayCnt = 0;
                D_TMR1_Enable(true);
              
            // Unlock doors    
            } else if (strcmp(read.command, COMMAND_UNLOCK) == 0) {
                lockDelayCnt = 0;
                D_TMR1_Enable(false);
                C_DOOR_UnlockAll();
                
            // Initialize    
            } else if (strcmp(read.command, COMMAND_INIT) == 0) {
                initDoors((uint8_t)(*read.message - 0x30));
                running = true;
                
            // Reset    
            } else if (strcmp(read.command, COMMAND_RESET) == 0) {
                __delay_ms(20);
                Reset();
                
            // Ping    
            } else if (strcmp(read.command, COMMAND_PING) == 0) {
                D_UART_WriteInt(COMMAND_STATE, running);
                
            // Alarm    
            } else if (strcmp(read.command, COMMAND_ALARM) == 0) {
                setAlarm((uint8_t)(*read.message - 0x30));
                
            } else {
                D_UART_Write(COMMAND_ERROR, ERROR_UNKNOWN);
                
            }
        }
        
        // FSM
        if (tick) {
            tick = false;
            
            // LED
            if (ledDelayCnt < LED_DELAY) {
                ledDelayCnt++;
            } else {
                LED1 = !LED1;
                ledDelayCnt = 0;
            }
            
            // Read inputs
            C_DOOR_ReadSensors();
            
            // Send states
            C_DOOR_SendStates();
            
            // Communication check
            if (communicationCnt < ALARM_DELAY) {
                communicationCnt++;
            } else {
                if (oldAlarm == ALARM_OFF) {
                    newAlarm = ALARM_COMM;
                }
            }
            
            // Set PWM for alarm
            if (newAlarm != oldAlarm || newAlarm == ALARM_SOFT || newAlarm == ALARM_COMM) {
                switch (newAlarm) {
                    default:
                    case ALARM_OFF: 
                        pwm = PWM_OFF; 
                        break;
                        
                    case ALARM_COMM:
                    case ALARM_SOFT:  
                        if (pwm == 0) {
                            pwm = 1;
                        }
                        pwm = (uint8_t)(pwm * PWM_SOFT);
                        break;
                        
                    case ALARM_HARD: 
                        pwm = PWM_HARD; 
                        break;
                }
                D_PWM_SetPwm(pwm);
                oldAlarm = newAlarm;
            }
        }
    }
}

void interrupt low_priority LowISR(void) {
    // Finite state clock
    if (INTCONbits.TMR0IF) {
        INTCONbits.TMR0IF = 0;
        tick = true;
    }
    
    // Door lock delay timer
    if (PIR1bits.TMR1IF) {
        TMR1H = 0x5E;
        TMR1L = 0x00;
        
        if (lockDelayCnt < LOCK_DELAY-1) {
            lockDelayCnt++;
        } else {
            lock = true;
        }
        
        PIR1bits.TMR1IF = 0;
    }
}
