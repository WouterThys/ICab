#ifndef PORT_DRIVER_H
#define	PORT_DRIVER_H

    
// Ports for UART
#define UART_TX         PORTCbits.RC7
#define UART_RX         PORTCbits.RC6
    
#define UART_TX_Dir     TRISCbits.TRISC7
#define UART_RX_Dir     TRISCbits.TRISC6
    
// Door ports
#define DOORS_Port      LATB
#define DOORS_Dir       TRISB
    
#define SENSORS_Port    PORTB
#define SENSORS_Dir     TRISB
    
 /**
 * Initializes all the parameters to the default setting, as well as writing the
 * tri-state registers. Sets all ports to zero, and all tri-states to output.
 */
void D_PORT_Init();

#endif	/* PORT_DRIVER */