#include <xc.h>

#include "../Drivers/PORT_Driver.h"
#include "../Drivers/UART_Driver.h"
#include "DOOR_Controller.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/

/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/
#define PORT_MASK(d)        ~(1<<doors[d].port_pin) 

/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/
static Door doors[DOOR_COUNT];

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/
static void commandAndMessage(Door door, char* com, char* mes);

static void commandAndMessage(Door door, char* com, char* mes) {
    // Command
    com[0] = 'P';
    com[1] = door.id + 0x30;
    com[2] = '\0';
    
    // Message
    if (door.is_open) {
        mes[0] = 'O';   
    } else {
        mes[0] = 'C';   
    }
    mes[1] = '\0';
}

/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void C_DOOR_Init() {
    // Ports
    DOORS_Dir &= 0x3F; // PORT6 -> PORTB7 are port lock pins => outputs
    SENSORS_Dir |= 0x3F; // PORTB0 -> PORTB4 are sensor inputs => inputs 
    
    // Doors
    uint8_t d;
    for (d = 0; d < DOOR_COUNT; d++) {
        doors[d].id = d;
        
        doors[d].locked = true;
        doors[d].lock_port = &PORTB;
        doors[d].lock_pin = 7;
        
        doors[d].is_open = false;
        doors[d].was_open = false;
        doors[d].sensor_port = &PORTB;
        doors[d].sensor_pin = d;
    }
}

void C_DOOR_Lock(uint8_t id) {
    uint8_t d;
    for (d = 0; d < DOOR_COUNT; d++) {
        if (doors[d].id == id) {
            *doors[d].lock_port |= 1 << doors[d].lock_pin;
        }
    }
}
    
void C_DOOR_LockAll() {
//    uint8_t d;
//    for (d = 0; d < DOOR_COUNT; d++) {
//        *doors[d].lock_port |= 1 << doors[d].lock_pin;
//    }
    *doors[0].lock_port |= 0xC0;
}

void C_DOOR_Unlock(uint8_t id) {
    uint8_t d;
    for (d = 0; d < DOOR_COUNT; d++) {
        if (doors[d].id == id) {
            *doors[d].lock_port &= ~(1 << doors[d].lock_pin);
        }
    }
}

void C_DOOR_UnlockAll() {
//    uint8_t d;
//    for (d = 0; d < DOOR_COUNT; d++) {
//        *doors[d].lock_port &= ~(1 << doors[d].lock_pin);
//    }
    *doors[0].lock_port &= 0x3F;
}

void C_DOOR_ReadSensors() {
    uint8_t d;
    for (d = 0; d < DOOR_COUNT; d++) {
        doors[d].is_open = (*doors[d].sensor_port >> doors[d].sensor_pin) & 0x01;
    }
}

void C_DOOR_SendStates() {
    uint8_t d;
    for (d = 0; d < DOOR_COUNT; d++) {
        if (doors[d].was_open != doors[d].is_open) {
            
            char com[3];
            char mes[2];
            commandAndMessage(doors[d], com, mes);

            // Message
            D_UART_Write(com, mes);
            
            doors[d].was_open = doors[d].is_open;
        }
    }
}