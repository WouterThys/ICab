
#ifndef XC_PWM_H
#define	XC_PWM_H

#include <xc.h> // include processor files - each processor file is guarded.  
#include <stdint.h>

void D_PWM_Init();

void D_PWM_SetPwm(uint8_t pwm);

#endif	/* XC_HEADER_TEMPLATE_H */

