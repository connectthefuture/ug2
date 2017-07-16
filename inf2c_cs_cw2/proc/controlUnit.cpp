// Inf2C Computer Systems, Processor design practical
// Copyright 2004-2012, School of Informatics, The University of Edinburgh

// Control unit for processor. 

// Template file. 
// Only contains implementation for j, addi,lw and halt instructions.

#include "systemc.h"
#include "controlUnit.h"
#include "defines.h"

void controlUnit::ctrl_regs()
{
    sc_uint<3> t_next_cycle;

    while (1) {
        wait(); // wait for reset or clock

        if (reset) {
            wait(1, SC_NS);
            cur_cycle = 0;
            cycle_count = 0;
        } else {
            if (halt.read()) {
                // endSimulation
                std::cout << sc_time_stamp()
                          <<" Halting simulation!"
                          << std::endl
                          << "Cycles = "
                          << cycle_count
                          << std::endl;
                sc_stop();
                // No return instruction needed.
            }
            cycle_count++;
            t_next_cycle = next_cycle;
            wait(1, SC_NS);
            cur_cycle = t_next_cycle;
        }
    }
}



void controlUnit::ctrl_comb()
{
    sc_int<32> t_ir = ir;

    // Extract useful subfields of the instruction
    opcode = t_ir.range(31,26);
    subfunct = t_ir.range(5,0);

    // Default values for various control fields

    halt = false;
    mem_rd = false;
    mem_wrt = false;

    ldPC = false; 
    ldIR = false; 
    ldReg = false;

    ldMAR = false;
    ldALUout = false;
    ldMDLR = false;
    ldMDSR = false;

    // Defaults for multiplexer settings and ALU function.  While
    // defaults are set here, the main code should always make intent
    // clear by either setting values explicitly or adding appropriate
    // comments at the positions where the values matter.

    wreg_addr_mux = WA_RD;  
    pc_mux = PC_ALU; 
    addr_mux = ADDR_PC;  
    a_mux = A_PC;
    b_mux = B_REG;
    reg_write_mux = RW_ALU_OUT; 
    func = ADD;

    switch(cur_cycle.read()){

    //----------------------------------------------------------------------
    // CYCLE 0
    //----------------------------------------------------------------------
    case(0): 
        // Instruction fetch.  Same for all instructions
        next_cycle = 1; 
        mem_rd = true;
        ldPC = true;
        ldIR = true;
        pc_mux=PC_ALU;
        addr_mux = ADDR_PC;
        a_mux = A_PC;
        b_mux = B_4;
        func = ADD;
        break;
    //----------------------------------------------------------------------
    // CYCLE 1
    //----------------------------------------------------------------------
    case(1):
        if (opcode == 0x0 && subfunct == 0xc) {
            // halt instruction
            printf("Halt instruction\n");
            halt = true;
        }
        else if (opcode == 0x8) { // addi
            next_cycle = 2;
            ldALUout = true;
            a_mux = A_REG;
            b_mux = B_IR_16;
            func = ADD;
        }
        else if (opcode == 0x23) { // lw 
            next_cycle = 2;
            ldMAR = true;
            a_mux = A_REG;
            b_mux = B_IR_16;
            func = ADD;
        }
        else if (opcode == 0x2) { // jump 
            next_cycle = 0;  
            ldPC = true;  
            pc_mux = PC_IMM;
        }
        else {
            // unrecognised instruction
            std::cout << "At " << sc_time_stamp() << ": ";
            printf("unrecognized instruction (opcode=0x%x, subfunct=0x%x)",
                   opcode,
                   subfunct);
            std::cout << ", cycle 1" << std::endl;
            halt = true;
        }
        break;
    //----------------------------------------------------------------------
    // CYCLE 2
    //----------------------------------------------------------------------
    case (2): 
        if (opcode == 0x8  ) { // addi 

            next_cycle = 0;
            ldReg = true;
            wreg_addr_mux = WA_RT;
            reg_write_mux = RW_ALU_OUT;
            
        }
        else if (opcode == 0x23) { // lw
            next_cycle = 3;
            mem_rd = true;
            ldMDLR = true;
            addr_mux = ADDR_MAR;
        }
        else {
            // unrecognised instruction
            std::cout << "At " << sc_time_stamp() << ": ";
            printf("unrecognized instruction (opcode=0x%x, subfunct=0x%x)",
                opcode,
                subfunct);
            std::cout << ", cycle 2" << std::endl;
            halt = true;
        }
        break;
    //----------------------------------------------------------------------
    // CYCLE 3
    //----------------------------------------------------------------------
    case(3): 
        if (opcode == 0x23) { // lw
            next_cycle = 0;
            ldReg = true;
            wreg_addr_mux = WA_RT;
            reg_write_mux = RW_MEM;
        }
        else {
            // unrecognised instruction
            std::cout << "At " << sc_time_stamp() << ": ";
            printf("unrecognized instruction (opcode=0x%x, subfunct=0x%x)",
                opcode,
                subfunct);
            std::cout << ", cycle 3" << std::endl;
            halt = true;
        }
        break;
        
    //----------------------------------------------------------------------
    // CYCLE 4+
    //----------------------------------------------------------------------
    default: 
        std::cout << "At " << sc_time_stamp() << ": ";
        std::cout << "unexpected current state " << cur_cycle.read();
        std::cout << std::endl;
        halt = true;

    } // end switch(cur_state)

    return;
}
