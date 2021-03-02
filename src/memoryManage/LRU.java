package memoryManage;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @ClassName: LRU
 * @Description: 在进程发生缺页中断时，使用LRU算法进行页面的淘汰
 * @Author: Lu Ning
 * @Date: 2021/2/26 23:45
 * @Version: v1.0
 */
public class LRU {

    /**
     * @Description: 利用栈算法实现LRU
     * @param: [recodeStack, comingPage]
     * @return: short  返回的是被淘汰的页号
     * @auther: Lu Ning
     * @date: 2021/3/4 00:46
     */
    public static short lru(Stack<Short> recodeStack, short comingPage){
        if (recodeStack.size() < 3){
            recodeStack.push(comingPage);
            return (short) -recodeStack.size();//内存没满，直接置入新页，但引发缺页中断
        }
        ArrayList<Short> arrayList = new ArrayList<>();
        short returnShort;
        //一个栈放已有的，访问某页时，不断出栈至找到这页（没有就等于全出栈了），之后抛弃最后一个出栈的，入栈剩下的，最后把新的压到栈顶
        while (recodeStack.peek() != comingPage) {
            arrayList.add(recodeStack.pop());
            if (recodeStack.isEmpty()) {
                break;
            }
        }
        if(!recodeStack.isEmpty())  {
            recodeStack.pop();
            returnShort = -10;    //不需要换页
        }else {
            returnShort = arrayList.remove(arrayList.size() - 1);  //需要换页，引发缺页中断
        }
        arrayList.add(0,comingPage);
        while (!arrayList.isEmpty()){
            recodeStack.push(arrayList.remove(arrayList.size() - 1));
        }
        return returnShort;
    }

    public static void main(String[] args) {
        Stack<Short> stack = new Stack<>();
        stack.push((short)1);
//        stack.push((short)2);

        lru(stack,(short) 7);
        System.out.println(stack);
    }

}
