package workManage;

import control.GUI;
import deviceManage.Spooling;
import hardware.Block;
import hardware.CPU;
import memoryManage.LRU;
import memoryManage.MissingPageInterruptThread;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @ClassName: Instruction
 * @Description: 指令类
 * @Author: luning
 * @Date: 2021/2/28 22:19
 * @Version: v1.0
 */
public class Instruction {

    public short getAddress() {
        return address;
    }

    public void setAddress(short address) {
        this.address = address;
    }

    private short address;          //指令虚拟地址
    private String instructionType;  //操作数类型
    private String p0;                //第0个操作数
    private String p1;                //第1个操作数

    /**
     * @Description: 从文件中读取并生成指令
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/28 22:27
     */
    public Instruction(short[] context) throws Exception {
        System.out.println(Arrays.toString(context));
        if(context[0] == 0 && context[1] == 0 && context[2] == 0 && context[3] == 0
            ||context[0]<'0' || context[0]>'9') {
            System.out.println("结束点1");
            return;
        }
        BigInteger bigInteger = new BigInteger(Block.convertShortToUTF16(context[0])+Block.convertShortToUTF16(context[1])
                +Block.convertShortToUTF16(context[2])+Block.convertShortToUTF16(context[3]),16);
        address = bigInteger.shortValue();
        System.out.println(address+"ddd");
        String tempType = Block.convertShortToUTF16(context[6]) + Block.convertShortToUTF16(context[7]) + Block.convertShortToUTF16(context[8]);
        String tempP0= Block.convertShortToUTF16(context[10]) + Block.convertShortToUTF16(context[11]);
        String tempP1= Block.convertShortToUTF16(context[13]) + Block.convertShortToUTF16(context[14]);
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
                    p1= "00";
                    System.out.println("不合法的第1个操作数"+tempType+"已转化为00");
                }
                break;
            case "out":
            case "inn":
            case "rea":
            case "wri":
            case "sle":
                instructionType = tempType;
                p0 = "";
                p1 = "";
                break;
            case "pri":
            case "jmp":
            case "jmz":
                instructionType = tempType;
                if (tempP0.equals("r0") || tempP0.equals("r1") || tempP0.equals("r2") || tempP0.equals("r3")) {
                    p0 = tempP0;
                } else {
                    p0= "r0";
                    System.out.println("不合法的第0个操作数"+tempType+"已转化为r0");
                }
                p1 = "";
                break;
            case "jmb":
                instructionType = tempType;
                try {
                    Short.valueOf(tempP0);    //如果报错说明不是纯数字
                    p0 = tempP0;
                } catch (Exception e) {
                    p0= "00";
                    System.out.println("不合法的第0个操作数"+tempType+"已转化为00");
                }
                p1 = "";
                break;
            default:
                instructionType="sle";
                p0 = "";
                p1 = "";
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
            case "jmp":
                temp[0] = 14;
                break;
            case "jmz":
                temp[0] = 15;
                break;
            case "jmb":
                temp[0] = 16;
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
            default:
                temp[0] += 9;
        }
        switch (instruction.p1) {
            case "r0":
                temp[0] += 0;
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
//                temp[1] += Short.parseShort(instruction.p1);
                temp[1] += 9;
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
    public static void executeInstruction(Instruction instruction) throws Exception {
        //显示物理地址
        if(instruction == null){
            Primitives.destroy(CPU.workingProcess);
            GUI.outInfoArea.append("指令执行完毕，进入中止态\n");
            return;
        }

        if(instruction.instructionType.equals("")){
            //满足此条件是因为已经被标记了缺页中断
            setCpuRegister("cx", (short) (getCpuRegister("cx")-1));   //补偿多加的
            String res = "当前逻辑地址"+Block.convertShortToAddress((short) (getCpuRegister("pc")))+"由于缺页中断申请访问硬盘交换区\n";
            GUI.outInfoArea.append(res);
            PV.PDisk(CPU.workingProcess);

            return;
        }
        //-8是因为获取指令到时候pc已经自动+8了。
        String res = "当前逻辑地址"+Block.convertShortToAddress((short) (getCpuRegister("pc")))+",执行了"+instruction.instructionType+"情况如下：\n";
        GUI.outInfoArea.append(res);
        res = "";
        //根据不同指令执行命令
        switch (instruction.instructionType) {
            case "add":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) + getCpuRegister(instruction.p1)));
                res += instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                GUI.outInfoArea.append(res);
                break;
            case "sub":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) - getCpuRegister(instruction.p1)));
                res += instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                GUI.outInfoArea.append(res);
                break;
            case "mul":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) * getCpuRegister(instruction.p1)));
                res += instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                GUI.outInfoArea.append(res);
                break;
            case "div":
                setCpuRegister(instruction.p0, (short) (getCpuRegister(instruction.p0) / getCpuRegister(instruction.p1)));
                res += instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                GUI.outInfoArea.append(res);
                break;
            case "mov":
                setCpuRegister(instruction.p0, Short.parseShort(instruction.p1));
//                short[] temp = CPU.workingProcess.getTempFile().readInstruction(getCpuRegister("pc"));
//                int value =  (temp[6]-'0')+10*(temp[5]-'0')+100*(temp[4]-'0')+1000*(temp[3]-'0')+10000*(temp[2]-'0');
//                setCpuRegister(instruction.p0, (short) value);
//                setCpuRegister("pc", (short) (getCpuRegister("pc")+8));
                res += instruction.p0 + "<-" + getCpuRegister(instruction.p0);
                GUI.outInfoArea.append(res);
                break;
            case "out":
                res += "尝试使CPU进入核心态去,去P操作申请显示器\n";
                GUI.outInfoArea.append(res);
                PV.PDisplay(CPU.workingProcess);
                CPU.setCpuWork(false);
                break;
            case "inn":
                //P操作，申请一个键盘资源
                res += "尝试使CPU进入核心态去,去P操作申请键盘资源\n";
                GUI.outInfoArea.append(res);
                PV.PKeyboard(CPU.workingProcess);
                CPU.setCpuWork(false);
                break;
            case "rea":
                //P操作，申请访问磁盘
                res += "尝试使CPU进入核心态,去读取磁盘文件\n";
                GUI.outInfoArea.append(res);
                PV.PDisk(CPU.workingProcess);
                CPU.setCpuWork(false);
                break;
            case "wri":
                //P操作，申请访问磁盘
                res += "尝试使CPU进入核心态,去写入磁盘文件\n";
                GUI.outInfoArea.append(res);
                PV.PDisk(CPU.workingProcess);
                CPU.setCpuWork(false);
                break;
            case "pri":
                res += "请求打印机服务，准备向缓冲区和输入井提交信息\n";
                GUI.outInfoArea.append(res);
                Spooling.typeAHead(CPU.workingProcess,(short) 3, String.valueOf(getCpuRegister(instruction.p0)));
                break;
            case "sle":
                res += "在这个时间片不做任何操作\n";
                GUI.outInfoArea.append(res);
                break;
            case "le<":
                if(getCpuRegister(instruction.p0) < getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                    res += "条件成立，zf标志位为1\n";
                }else {
                    setCpuRegister("psw",(short) 0);
                    res += "条件不成立，zf标志位为0\n";

                }
                GUI.outInfoArea.append(res);
                break;
            case "gr>":
                if(getCpuRegister(instruction.p0) > getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                    res += "条件成立，zf标志位为1\n";

                }else {
                    setCpuRegister("psw", (short) 0);
                    res += "条件不成立，zf标志位为0\n";
                }
                GUI.outInfoArea.append(res);
                break;
            case "equ":
                if(getCpuRegister(instruction.p0) == getCpuRegister(instruction.p1)){
                    setCpuRegister("psw",(short) 1);
                    res += "条件成立，zf标志位为1\n";

                }else {
                    setCpuRegister("psw",(short) 0);
                    res += "条件不成立，zf标志位为0\n";
                }
                GUI.outInfoArea.append(res);
                break;
            case "jmz":
                if(getCpuRegister("psw") == 0){
                    break;
                }
            case "jmp":
                short address = getCpuRegister(instruction.p0);
                if(address % 16 != 0) {
                    address += 16 - address % 16;
                }
                if(address <0 || address/256+1 >= CPU.workingProcess.getExchangeFile().getfInode().getFileSize() ||
                        CPU.workingProcess.getExchangeFile().readInstruction(address/16)[0]==0 ||
                        CPU.workingProcess.getExchangeFile().readInstruction(address/16)[0]==-1 ){
                    CPU.setCpuWork(false);
                    MissingPageInterruptThread.setNeedMMUWork(false);
                    Primitives.destroy(CPU.workingProcess);
                    res = "跳跃指令位置过远，进程强行中止\n";
                    GUI.outInfoArea.append(res);
                    return ;
                }
                res = "即将跳跃到逻辑地址"+Block.convertShortToAddress(address)+'\n';
                setCpuRegister("pc",address);
                setCpuRegister("cx", (short) (address/16));
                GUI.outInfoArea.append(res);
                if(!CPU.workingProcess.getUserStack().contains(((short)(CPU.getPc()/256)))){
                    short newPage = (short) (CPU.getPc()/256);
                    short oldPage = LRU.lru(CPU.workingProcess.getUserStack(), newPage);
                    if(oldPage > -5){
                        //两种情况，都在下面缺页中断相关方法中处理
                        //引发缺页中断,在页表进行替换 或 引发缺页中断但内存未满，加载新页即可

                        //向缺页中断线程发起缺页中断请求，由此线程处理缺页中断
                        MissingPageInterruptThread.callMissingPage(newPage,oldPage,CPU.workingProcess);
                        res = "由于跳转后的下一条指令会缺页中断申请访问硬盘交换区\n";
                        PV.PDisk(CPU.workingProcess);
                        GUI.outInfoArea.append(res);
                        return;
                    }
                }
                break;
            case "jmb":
                address = (short) (Short.parseShort(instruction.p0)*256);

                if(address <0 || address/256+1 >= CPU.workingProcess.getExchangeFile().getfInode().getFileSize() ||
                        CPU.workingProcess.getExchangeFile().readInstruction(address/16)[0]==0 ||
                        CPU.workingProcess.getExchangeFile().readInstruction(address/16)[0]==-1 ){
                    CPU.setCpuWork(false);
                    MissingPageInterruptThread.setNeedMMUWork(false);
                    Primitives.destroy(CPU.workingProcess);
                    res = "跳跃指令位置过远，进程强行中止\n";
                    GUI.outInfoArea.append(res);
                    return ;
                }
                res = "即将跳跃到第"+instruction.p0+"逻辑块，逻辑地址为"+Block.convertShortToAddress(address)+'\n';
                setCpuRegister("pc",address);
                setCpuRegister("cx", (short) (address/16));
                GUI.outInfoArea.append(res);
                if(!CPU.workingProcess.getUserStack().contains(((short)(CPU.getPc()/256)))){
                    short newPage = (short) (CPU.getPc()/256);
                    short oldPage = LRU.lru(CPU.workingProcess.getUserStack(), newPage);
                    if(oldPage > -5){
                        //两种情况，都在下面缺页中断相关方法中处理
                        //引发缺页中断,在页表进行替换 或 引发缺页中断但内存未满，加载新页即可

                        //向缺页中断线程发起缺页中断请求，由此线程处理缺页中断
                        MissingPageInterruptThread.callMissingPage(newPage,oldPage, CPU.workingProcess);
                        res = "由于跳转后的下一条指令会缺页中断申请访问硬盘交换区\n";
                        PV.PDisk(CPU.workingProcess);
                        GUI.outInfoArea.append(res);
                        return;
                    }
                }
                break;
        }
//        GUI.outInfoArea.append("\n");
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
                break;
            case "cx":
                CPU.setCx(value);
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
            case "cx":
                return CPU.getCx();
        }
        throw new Exception("尝试获取错误的寄存器");
    }


    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getP0() {
        return p0;
    }

    public void setP0(String p0) {
        this.p0 = p0;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }



    public static void main(String[] args) {
        BigInteger bigInteger = new BigInteger(String.valueOf("0")+String.valueOf("f")+String.valueOf("0")+String.valueOf("0"),16);
        System.out.println(2373%64);
    }
}
