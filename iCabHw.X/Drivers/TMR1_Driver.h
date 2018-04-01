#ifndef TMR1_DRIVER_H
#define	TMR1_DRIVER_H

#include <stdint.h>
#include <xc.h>
#include <stdbool.h>

/**
 * Initialize timer 0
 */
void D_TMR1_Init();

/**
 * Enable timer 0
 * @param enable
 */
void D_TMR1_Enable(bool enable);

#endif	/* TMR0_DRIVER_H */

