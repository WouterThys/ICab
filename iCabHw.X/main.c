#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"
#include "Drivers/TMR0_Driver.h"
#include "Drivers/PWM_Driver.h"
#include "Controllers/DOOR_Controller.h"

#define _XTAL_FREQ 16000000 /* 16 MHz crystal                                 */

#define COMMAND_LOCK    "L" /* Command to lock doors                          */
#define COMMAND_UNLOCK  "U" /* Command to unlock doors                        */
#define COMMAND_INIT    "I" /* Command to initialize                          */
#define COMMAND_RESET   "R" /* Command to reset PIC                           */
#define COMMAND_PING    "P" /* Command to ping PIC                            */
#define COMMAND_ERROR   "E" /* Command to indicate error                      */
#define COMMAND_ALARM   "A" /* Command to set alarm                           */

#define ERROR_UNKNOWN   "U" /* Error message to send when unknown command     */

#define ALARM_OFF     0
#define ALARM_SOFT    1
#define ALARM_HARD    2

#define PWM_OFF     0x00
#define PWM_SOFT    0x0A
#define PWM_HARD    0x7F

static READ_Data read;
static bool tick;

static uint8_t pwm;
static uint8_t newAlarm;
static uint8_t oldAlarm;

static void initDoors(uint8_t door_cnt);
static void setAlarm(uint8_t alarm);

static void initDoors(uint8_t door_cnt) {
    if (door_cnt < 1) {
        door_cnt = 1;
    }
    // Doors
    C_DOOR_Init(door_cnt);
    C_DOOR_LockAll();
    
    // Start timer
    D_TMR0_Enable(true);
}

static void setAlarm(uint8_t alarm) {
    newAlarm = alarm;
}

void main(void) {
    __delay_ms(200);
    // Initialize ports to defaults
    D_PORT_Init();
    
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init("P", 9600, true);
    D_UART_Enable(true);
    
    // Initialize timer
    D_TMR0_Init();
    
    // Initialize PWM
    D_PWM_Init();
    
    __delay_ms(200);
    
    newAlarm = 0;
    oldAlarm = 0;
    
    while(1) {
        
        // Serial
        if (readReady) {
            readReady = false;
            read = D_UART_Read();
            if (strcmp(read.command, COMMAND_LOCK) == 0) {
                C_DOOR_LockAll();
            } else if (strcmp(read.command, COMMAND_UNLOCK) == 0) {
                C_DOOR_UnlockAll();
            } else if (strcmp(read.command, COMMAND_INIT) == 0) {
                initDoors((uint8_t)(*read.message - 0x30));
            } else if (strcmp(read.command, COMMAND_RESET) == 0) {
                __delay_ms(20);
                Reset();
            } else if (strcmp(read.command, COMMAND_PING) == 0) {
                // Do nothing, acknowledge message will be send automatically
            } else if (strcmp(read.command, COMMAND_ALARM) == 0) {
                setAlarm((uint8_t)(*read.message - 0x30));
            } else {
                D_UART_Write(COMMAND_ERROR, ERROR_UNKNOWN);
            }
        }
        
        // FSM
        if (tick) {
            tick = false;
            
            // Read inputs
            C_DOOR_ReadSensors();
            
            // Send states
            C_DOOR_SendStates();
            
            // Set PWM for alarm
            if (newAlarm != oldAlarm || newAlarm == ALARM_SOFT) {
                switch (newAlarm) {
                    default:
                    case 0: pwm = PWM_OFF; break;
                    case 1: pwm += PWM_SOFT; break;
                    case 2: pwm = PWM_HARD; break;
                }
                D_PWM_SetPwm(pwm);
                oldAlarm = newAlarm;
            }
        }
    }
}

void interrupt HighISR(void) {
    if (INTCONbits.TMR0IF) {
        tick = true;
        INTCONbits.TMR0IF = 0;
    }
}
