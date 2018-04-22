#ifndef CONFIG_H
#define	CONFIG_H

#include <xc.h> // include processor files - each processor file is guarded.  

#define _XTAL_FREQ 16000000UL /* 16 MHz crystal                                 */
/*******************************************************************************
 *                      ALARM SETTINGS 
 ******************************************************************************/

/**
 * PWM signal for alarm. 
 *  - PWM_OFF is the value for no signal
 */
#define PWM_OFF     0x00

/**
 * PWM signal for alarm.
 *  - PWM_SOFT is the multiplier which will be used to increase the PWM value. 
 *    The new PWM value will be PWM * @PWM_SOFT
 */
#define PWM_SOFT    0x02

/**
 * PWM signal for alarm.
 *  - PWM_HARD is the value for the loudest continuous alarm.
 */
#define PWM_HARD    0x7F




/*******************************************************************************
 *                      SERIAL SETTINGS 
 ******************************************************************************/

/**
 * Baud rate of serial communication
 */
#define SERIAL_BAUD 9600




/*******************************************************************************
 *                      DOOR SETTINGS 
 ******************************************************************************/

/**
 * LOCK and UNLOCK signal polarity
 */
#define LOCK    0
#define UNLOCK  !LOCK

/**
 * OPEN and CLOSED sensor signal polarity
 */
#define OPEN    1
#define CLOSED  !OPEN

/*******************************************************************************
 *                      ALARM SETTINGS 
 ******************************************************************************/

/**
 * Delay between a LOCK_ALL signal and the effective locking of the ports.
 * The LOCK_DELAY is a multiple of 10 ms: doors will lock after 
 * LOCK_DELAY * 10 ms
 */
#define LOCK_DELAY  100

/**
 * The alarm will go off when the ALARM_DELAY is exceeded, this happens when 
 * the controller does not receive any messages from the application before the 
 * ALARM_DELAY count.
 * The ALARM_DELAY is a multiple of 100 ms: alarm will go of after
 * ALARM_DELAY * 100 ms
 */
#define ALARM_DELAY  60

#endif	/* CONFIG_H */

