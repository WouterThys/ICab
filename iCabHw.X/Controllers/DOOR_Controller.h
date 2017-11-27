

// This is a guard condition so that contents of this file are not included
// more than once.  
#ifndef XC_HEADER_TEMPLATE_H
#define	XC_HEADER_TEMPLATE_H

#include <xc.h> // include processor files - each processor file is guarded.  
#include <stdint.h>
#include <stdbool.h>

// PORTB0 and PORTB1 are lock pins
#define LOCK    0x03    
#define UNLOCK  0x00    

    
    
    typedef struct {
        uint8_t  id;
        
        bool     locked;        /* Application should clear or set this       */
        uint8_t* lock_port;     /* Port with pins for locking                 */
        uint8_t  lock_pin;      /* Pin of port for locking                    */
        
        bool     was_open;      /* Previous open/closed state of the door     */
        bool     is_open;       /* Last open/closed state of the door         */
        uint8_t* sensor_port;   /* Port with pins for sensors                 */
        uint8_t  sensor_pin;    /* Pin of port for sensor                     */
    }Door; 
    
    /**
     * Initialize doors
     * @param: number of doors
     */
    void C_DOOR_Init(uint8_t door_cnt);
    
    /**
     * Lock a door
     * @param id: id of the door
     */
    void C_DOOR_Lock(uint8_t id);
    
    /**
     * Lock all doors
     */
    void C_DOOR_LockAll();
    
    /**
     * Unlock a door
     * @param id: id of the door
     */
    void C_DOOR_Unlock(uint8_t id);
    
    /**
     * Unlock all doors
     */
    void C_DOOR_UnlockAll();
    
    /**
     * Read input sensors
     */
    void C_DOOR_ReadSensors();
    
    /**
     * Send door sensor states via UART module if door state has changed.
     */
    void C_DOOR_SendStates();
    

#endif	/* XC_HEADER_TEMPLATE_H */

