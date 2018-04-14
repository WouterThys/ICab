#include <xc.h>

#include "../Config.h"
#include "../Drivers/PORT_Driver.h"
#include "../Drivers/UART_Driver.h"
#include "DOOR_Controller.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/

/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/
// Set the value (v) on port (p) bit (b) 
#define SetValue(p, b, v) p ^= (uint8_t)((-v ^ p) & (1 << b))
// Get bit (b) of port (p)
#define GetValue(p, b) ((p >> b) & 0x01)

/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/
static int door_cnt;
static Door_t doors[10];

/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/
static void commandAndMessage(Door_t door, char* com, char* mes);

static void commandAndMessage(Door_t door, char* com, char* mes) {
    // Command
    com[0] = 'D';
    com[1] = (uint8_t)(door.id + 0x30);
    com[2] = '\0';
    
    // Message
    if (door.is_open) {
        mes[0] = 'C';   
    } else {
        mes[0] = 'O';   
    }
    mes[1] = '\0';
}

/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void C_DOOR_Init(uint8_t cnt) {
    // Doors
    door_cnt = cnt;
    uint8_t d;
    for (d = 0; d < door_cnt; d++) {
        doors[d].id = d;
        
        doors[d].locked = true;
        doors[d].lock_port = &DOORS_Port;
        doors[d].lock_pin = (uint8_t)(DOORS_First + d);
        
        doors[d].is_open = OPEN;
        doors[d].was_open = CLOSED;
        doors[d].sensor_port = &SENSORS_Port;
        doors[d].sensor_pin = (uint8_t) (SENSORS_First + d);
        
        SetValue(DOORS_Dir, (DOORS_First + d), 0); // Set direction output
        SetValue(SENSORS_Dir, (SENSORS_First + d), 1); // Set direction input
    }
}

//void C_DOOR_Lock(uint8_t id) {
//    uint8_t d;
//    for (d = 0; d < door_cnt; d++) {
//        if (doors[d].id == id) {
//            SetValue(*doors[d].lock_port, doors[d].lock_pin, LOCK);
//        }
//    }
//}
    
void C_DOOR_LockAll() {
    uint8_t d;
    for (d = 0; d < door_cnt; d++) {
        SetValue(*doors[d].lock_port, doors[d].lock_pin, LOCK);
    }
}

//void C_DOOR_Unlock(uint8_t id) {
//    uint8_t d;
//    for (d = 0; d < door_cnt; d++) {
//        if (doors[d].id == id) {
//            SetValue(*doors[d].lock_port, doors[d].lock_pin, UNLOCK);
//        }
//    }
//}

void C_DOOR_UnlockAll() {
    uint8_t d;
    for (d = 0; d < door_cnt; d++) {
        SetValue(*doors[d].lock_port, doors[d].lock_pin, UNLOCK);
    }
}

void C_DOOR_ReadSensors() {
    uint8_t d;
    for (d = 0; d < door_cnt; d++) {
        doors[d].is_open = (uint8_t)(GetValue(*doors[d].sensor_port, doors[d].sensor_pin) == OPEN);
    }
}

void C_DOOR_SendStates() {
    uint8_t d;
    for (d = 0; d < door_cnt; d++) {
        if (doors[d].was_open != doors[d].is_open) {
            
            char com[3];
            char mes[2];
            commandAndMessage(doors[d], com, mes);

            // Message
            D_UART_Write(com, mes);
            __delay_ms(1);
            
            doors[d].was_open = doors[d].is_open;
        }
    }
}