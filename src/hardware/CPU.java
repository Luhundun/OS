package hardware;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: cpu
 * @Description: 模拟裸机的cpu
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:41
 * @Version: v1.0
 */
public class CPU {

    private short ir;  //ir寄存器
    private short pc;  //pc寄存器
    private short pwd;  //状态字寄存器
    private short r0;  //普通寄存器
    private short r1;  //普通寄存器
    private short r2;  //普通寄存器
    private short r3;  //普通寄存器


    public short getIr() {
        return ir;
    }

    public void setIr(short ir) {
        this.ir = ir;
    }

    public short getPc() {
        return pc;
    }

    public void setPc(short pc) {
        this.pc = pc;
    }

    public short getPwd() {
        return pwd;
    }

    public void setPwd(short pwd) {
        this.pwd = pwd;
    }

    public short getR0() {
        return r0;
    }

    public void setR0(short r0) {
        this.r0 = r0;
    }

    public short getR1() {
        return r1;
    }

    public void setR1(short r1) {
        this.r1 = r1;
    }

    public short getR2() {
        return r2;
    }

    public void setR2(short r2) {
        this.r2 = r2;
    }

    public short getR3() {
        return r3;
    }

    public void setR3(short r3) {
        this.r3 = r3;
    }




    public CPU(){

    }
}
