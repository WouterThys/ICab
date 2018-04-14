#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <xc.h>
#include <string.h>

#include "../Config.h"
#include "PORT_Driver.h"
#include "UART_Driver.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/
//#define _XTAL_FREQ 16000000UL
// &
#define START_CHAR 0x26 
// $
#define STOP_CHAR  0x24 
// :
#define SEP_CHAR   0x3A 

#define READ_STATE_START   0x00
#define READ_STATE_TYPE    0x01
#define READ_STATE_SENDER  0x02
#define READ_STATE_COMMAND 0x03
#define READ_STATE_MESSAGE 0x04
#define READ_STATE_END     0x05

/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/

/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/
uint8_t baud;

const char* startCharacter =       "&";
const char* stopCharacter =        "$";
const char* deviceName;
const char* messageCharacter =     "[M]";
const char* ackCharacter =         "[A]";

typedef struct {
    uint8_t type[10];     // Type of the message buffer  
    uint8_t typeCnt;      // Count of the type buffer
    uint8_t sender[50];   // Sender name buffer
    uint8_t senderCnt;    // Count of the sender buffer
    uint8_t command[50];  // Command buffer
    uint8_t commandCnt;   // Count of the command buffer
    uint8_t message[50];  // Message buffer
    uint8_t messageCnt;   // Count of the message buffer
    uint8_t readId;       // Id send from the sender, to acknowledge
    uint8_t state;    // State of the read buffer
} READ_Buffer;
READ_Buffer readBuffer;

READ_Data readData;

bool readReady;

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/

static void fillDataBuffer(uint8_t data);
static void acknowledge();

static void fillDataBuffer(uint8_t data){
    switch(readBuffer.state) {
        case READ_STATE_START:
            if(data == START_CHAR) {
                readBuffer.typeCnt = 0;
                readBuffer.senderCnt = 0;
                readBuffer.commandCnt = 0;
                readBuffer.messageCnt = 0;
                readReady = false;
                readBuffer.state = READ_STATE_TYPE;
            } else {
                readBuffer.state = READ_STATE_START;
                return;
            }
            break;
        
        case READ_STATE_TYPE:
            if (data == SEP_CHAR) {
                readBuffer.state = READ_STATE_SENDER;
                readBuffer.type[readBuffer.typeCnt] = '\0';
            } else {
                readBuffer.type[readBuffer.typeCnt] = data;
                readBuffer.typeCnt++;
                if(readBuffer.typeCnt > 10) {
                    readBuffer.typeCnt = 0;
                }
            }
            break;
            
        case READ_STATE_SENDER:
            if (data == SEP_CHAR) {
                readBuffer.state = READ_STATE_COMMAND;
                readBuffer.sender[readBuffer.senderCnt] = '\0';
            } else {
                readBuffer.sender[readBuffer.senderCnt] = data;
                readBuffer.senderCnt++;
                if(readBuffer.senderCnt > 50) {
                    readBuffer.senderCnt = 0;
                }
            }
            break;
            
        case READ_STATE_COMMAND:
            if (data == SEP_CHAR) {
                readBuffer.state = READ_STATE_MESSAGE;
                readBuffer.command[readBuffer.commandCnt] = '\0';
            } else {
                readBuffer.command[readBuffer.commandCnt] = data;
                readBuffer.commandCnt++;
                if(readBuffer.commandCnt > 50) {
                    readBuffer.commandCnt = 0;
                }
            }
            break;
            
        case READ_STATE_MESSAGE:
            if (data == SEP_CHAR) {
                readBuffer.state = READ_STATE_END;
                readBuffer.message[readBuffer.messageCnt] = '\0';
            } else {
                readBuffer.message[readBuffer.messageCnt] = data;
                readBuffer.messageCnt++;
                if(readBuffer.messageCnt > 50) {
                    readBuffer.messageCnt = 0;
                }
            }
            break;
            
        case READ_STATE_END:
            if (data == STOP_CHAR) {
                acknowledge();
                readBuffer.state = READ_STATE_START;
                readReady = true;
            } else {
                // Convert from (ASCII)char to integer.
                readBuffer.readId = (uint8_t)(data - 0x30);
            }
            break;
            
        default: 
            readBuffer.state = READ_STATE_START;
            break;
    }
}

static void acknowledge() {
    printf(startCharacter);
    
    printf(ackCharacter);
    // id
    printf("%x",readBuffer.readId);
    
    printf(stopCharacter);
}

/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void D_UART_Init(const char* name, uint16_t baud, bool interrupts) {  
    // Clear/set variables
    deviceName = name;
    readReady = false;
    
    // Disable UART while initializing
    D_UART_Enable(false);
    
    // Clear
    RCSTAbits.FERR = 0;
    RCSTAbits.OERR = 0;
    RCREG = 0x00;
        
    // TXSTA register settings
    TXSTAbits.TX9 = 0; // Selects 8-bit transmission
    TXSTAbits.SYNC = 0; // Synchronous mode
    TXSTAbits.BRGH = 0; // Low speed
    
    // RCSTA register settings
    RCSTAbits.RX9 = 0; // Selects 8-bit reception

    // BAUDCON register settings
    BAUDCONbits.RXDTP = 0; // RX data is inverted
    BAUDCONbits.TXCKP = 0; // TX data is inverted
    BAUDCONbits.BRG16 = 0; // 8-bit Baud Rate Generator
    
    // Baud
    SPBRG = ((_XTAL_FREQ/baud)/64)-1; // Baud rate selection
    
    // Interrupts for reading
    if (interrupts) {
        RCONbits.IPEN = 1;   // Enable priority levels on interrupts
        INTCONbits.GIEH = 1; // Enable high interrupt
        INTCONbits.GIEL = 1; // Enable low interrupt
        PIR1bits.RCIF = 0; // Clear flag
        IPR1bits.RCIP = 1; // High priority
        PIE1bits.RCIE = 1; // Enable UART interrupt
    }
}

void D_UART_Write(const char* command, const char* data) {
    printf(startCharacter);
    
    printf(messageCharacter);
    printf(deviceName);
    // Command
    printf(":");printf(command);
    // Message
    printf(":");printf(data);
    
    printf(stopCharacter);
    __delay_ms(1);
}

void D_UART_WriteInt(const char* command, int d) {
    printf(startCharacter);
    
    printf(messageCharacter);
    printf(deviceName);
    // Command
    printf(":");printf(command);
    // Message
    printf(":");printf("%d", d);
    
    printf(stopCharacter);
    __delay_ms(1);
}

READ_Data D_UART_Read(){
    readData.sender = readBuffer.sender;
    readData.command = readBuffer.command;
    readData.message = readBuffer.message;
    return readData;
}

void D_UART_Enable(bool enable) {
    if(enable) {
        UART_TX_Dir = 0;
        UART_RX_Dir = 1;
        TXSTAbits.TXEN = 1; // Activate TX
        RCSTAbits.CREN = 1; // Activate RX
        RCSTAbits.SPEN = 1; // Enable UART
    } else {
        UART_TX_Dir = 0;
        UART_RX_Dir = 0;
        TXSTAbits.TXEN = 0; // Deactivate TX
        RCSTAbits.CREN = 0; // Deactivate RX
        RCSTAbits.SPEN = 0; // Enable UART
    }
}

void putch(char data) {
    uint8_t max = 0;
    // Wait while buffer is still full
    while(TXSTAbits.TRMT == 0 && max < 200) {
        max++;
        __delay_us(5);
    } 
    TXREG = data;
//    while(TXSTAbits.TRMT == 0);
//    TXREG = data;
}

void interrupt HighISR(void) {
    if (PIR1bits.RC1IF) {
        PIR1bits.RC1IF = 0;
        
        // Framing error (can be updated by reading RCREG register and receiving next valid byte)
        if(RCSTAbits.FERR == 1) {
            RCREG = 0x00; 
            return;
        }
        // Overrun error (can be cleared by clearing bit CREN)
        if(RCSTAbits.OERR == 1) {
            RCSTAbits.CREN = 0;
            RCSTAbits.CREN = 1;
            return;
        }
        fillDataBuffer(RCREG);
    }
}

