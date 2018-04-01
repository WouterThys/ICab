#ifndef PORT_DRIVER_H
#define	PORT_DRIVER_H

    
// Ports for UART
#define UART_TX         PORTCbits.RC7
#define UART_RX         PORTCbits.RC6
    
#define UART_TX_Dir     TRISCbits.TRISC7
#define UART_RX_Dir     TRISCbits.TRISC6
    
// Door ports
#define DOORS_Port      PORTB   /* Doors port                                 */
#define DOORS_Dir       TRISB   /* Direction of doors prot                    */
#define DOORS_First     0       /* First pin of the doors port is B0          */
    
#define SENSORS_Port    PORTA   /* Sensort port                               */
#define SENSORS_Dir     TRISA   /* Direction of sensor port                   */
#define SENSORS_First   1       /* First pin of the sensor port is A1         */
    
 /**
 * Initializes all the parameters to the default setting, as well as writing the
 * tri-state registers. Sets all ports to zero, and all tri-states to output.
 */
void D_PORT_Init();

#endif	/* PORT_DRIVER */