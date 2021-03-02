package workManage;

import control.OS;
import hardware.Block;
import hardware.CPU;

/**
 * @ClassName: Instruction
 * @Description: 指令类
 * @Author: luning
 * @Date: 2021/2/28 22:19
 * @Version: v1.0
 */
public class Instruction {
    public String instructionType;  //操作数类型
    public String p0;                //第0个操作数
    public String p1;                //第1个操作数

    /**
     * @Description: 从文件中读取并生成指令
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/28 22:27
     */
    public Instruction(short[] context) throws Exception {
        String tempType = Block.convertShortToUTF16(context[0]) + Block.convertShortToUTF16(context[1]) + Block.convertShortToUTF16(context[2]);
        String tempP0= Block.convertShortToUTF16(context[3]) + Block.convertShortToUTF16(context[4]);
        String tempP1= Block.convertShortToUTF16(context[5]) + Block.convertShortToUTF16(context[6]);
        //检查指令内容是否合法
        //加减乘除，两个操作数都是寄存器
        switch (tempType) {
            case "add":
            case "sub":
            case "mul":
            case "div":
            case "le<":
            case "gr>":
            case "equ":
                instructionType = tempType;
                if (tempP0.equals("r0") || tempP0.equals("r1") || tempP0.equals("r2") || tempP0.equals("r3")) {
                    p0 = tempP0;
                } else {
                    p0= "r0";
                    System.out.println("不合法的第0个操作数"+tempType+"已转化为r0");
                }
                if (tempP1.equals("r0") || tempP1.equals("r1") || tempP1.equals("r2") || tempP1.equals("r3")) {
                    p1 = tempP1;
                } else {
                    p1= "r0";
                    System.out.println("不合法的第1个操作数"+tempType+"已转化为r0");                }
                //mov，第0个是寄存器，第二个是立即数
                break;
            case "mov":
                instructionType = tempType;
                if (tempP0.equals("r0") || tempP0.equals("r1") || tempP0.equals("r2") || tempP0.equals("r3")) {
                    p0 = tempP0;
                } else {
                    p0= "r0";
                    System.out.println("不合法的第0个操作数"+tempType+"已转化为r0");                }
                try {
                    Short.valueOf(tempP1);    //如果报错说明不是纯数字
                    p1 = tempP1;
                } catch (Exception e) {
                    p1= "r0";
                    System.out.println("不合法的第1个操作数"+tempType+"已转化为r0");
                }
                break;
            case "out":
            case "inn":
            case "rea":
            case "wri":
            case "pri":
            case "sle":
                p0 = "";
                p1 = "";
                break;
            default:
                instructionType="sle";
                System.out.println("不合法的操作符"+tempType+"已转化为sle");
        }
    }
    
    /**
     * @Description: 将指令转化为short类型，一个文件中的指令转化后为2个字
     * @param: [instruction]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/28 23:20
     */
    public static short[] convertInstruction(Instruction instruction){
        short[] temp = new short[2];
        //根据指令类型转化short字节,指令类型为第1个字的高8位
        switch (instruction.instructionType) {
            case "add":
                temp[0] = 0;
                break;
            case "sub":
                temp[0] = 1;
                break;
            case "mul":
                temp[0] = 2;
                break;
            case "div":
                temp[0] = 3;
                break;
            case "mov":
                temp[0] = 4;
                break;
            case "out":
                temp[0] = 5;
                break;
            case "inn":
                temp[0] = 6;
                break;
            case "rea":
                temp[0] = 7;
                break;
            case "wri":
                temp[0] = 8;
                break;
            case "pri":
                temp[0] = 9;
                break;
            case "sle":
                temp[0] = 10;
                break;
            case "le<":
                temp[0] = 11;
                break;
            case "gr>":
                temp[0] = 12;
                break;
            case "equ":
                temp[0] = 13;
                break;
        }
        temp[0] = (short) (temp[0] * 256);
        switch (instruction.p0) {
            case "r0":
                temp[0] += 0;
                break;
            case "r1":
                temp[0] += 1;
                break;
            case "r2":
                temp[0] += 2;
                break;
            case "r3":
                temp[0] += 3;
                break;
        }
        switch (instruction.p1) {
            case "r0":
                temp[1] += 0;
                break;
            case "r1":
                temp[1] += 1;
                break;
            case "r2":
                temp[1] += 2;
                break;
            case "r3":
                temp[1] += 3;
                break;
            default:
                temp[1] = Short.parseShort(instruction.p1);
                break;
        }
        return temp;
    }

    /**
     * @Description: 执行指令的操作
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/2/28 23:35
     */
    public static String executeInstruction(Instruction instruction) throws Exception {
        String res = "";
        switch (instruction.instructionType) {
            case "add":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) + getCpuRegister(instruction.p1)));
                res = instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                break;
            case "sub":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) - getCpuRegister(instruction.p1)));
                res = instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                break;
            case "mul":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) * getCpuRegister(instruction.p1)));
                res = instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                break;
            case "div":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) / getCpuRegister(instruction.p1)));
                res = instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                break;
            case "mov":
                setCpuRegister(instruction.p0, Short.parseShort(instruction.p1));
                res = instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                break;
            case "out":

                break;
            case "inn":

                break;
            case "rea":

                break;
            case "wri":

                break;
            case "pri":

                break;
            case "sle":
                break;
            case "le<":
                if(getCpuRegister(instruction.p0) < getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                }else {
                    setCpuRegister("psw",(short) 0);
                }
                break;
            case "gr>":
                if(getCpuRegister(instruction.p0) > getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                }else {
                    setCpuRegister("psw", (short) 0);
                }
                break;
            case "equ":
                if(getCpuRegister(instruction.p0) == getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                }else {
                    setCpuRegister("psw",(short) 0);
                }
                break;
        }
        return "执行了"+instruction.instructionType+"  " + res;
    }

    /**
     * @Description: 模拟寄存器的计算
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/28 23:45
     */
    public static void setCpuRegister(String register, short value){
        switch (register) {
            case "r0":
                CPU.setR0(value);
                break;
            case "r1":
                CPU.setR1(value);
                break;
            case "r2":
                CPU.setR2(value);
                break;
            case "r3":
                CPU.setR3(value);
                break;
            case "ir":
                CPU.setIr(value);
                break;
            case "pc":
                CPU.setPc(value);
                break;
            case "psw":
                CPU.setPsw(value);
        }
    }

    /**
     * @Description: 模拟寄存器的计算
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/28 23:45
     */
    public static short getCpuRegister(String register) throws Exception {
        switch (register) {
            case "r0":
                return CPU.getR0();
            case "r1":
                return CPU.getR1();
            case "r2":
                return CPU.getR2();
            case "r3":
                return CPU.getR3();
            case "ir":
                return CPU.getIr();
            case "pc":
                return CPU.getPc();
            case "psw":
                return CPU.getPsw();
        }
        throw new Exception("尝试获取错误的寄存器");
    }





    public static void main(String[] args) {

    }
}
