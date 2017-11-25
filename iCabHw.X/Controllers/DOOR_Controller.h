

// This is a guard condition so that contents of this file are not included
// more than once.  
#ifndef XC_HEADER_TEMPLATE_H
#define	XC_HEADER_TEMPLATE_H

#include <xc.h> // include processor files - each processor file is guarded.  
#include <stdint.h>
#include <stdbool.h>

#define DOOR_COUNT  5

#ifdef	__cplusplus
extern "C" {
#endif /* __cplusplus */ 
    
    
    typedef struct {
        uint8_t id;
        bool    is_open;
        bool    is_locked;
    }Door; 
    
    /**
     * Initialize doors
     */
    void C_DOOR_Init();
    
    /**
     * Lock a door
     * @param id: id of the door
     */
    void C_DOOR_Lock(uint8_t id);
    
    /**
     * Lock all doors
     */
    void C_DOOR_LockAll();
    

#ifdef	__cplusplus
}
#endif /* __cplusplus */

#endif	/* XC_HEADER_TEMPLATE_H */

