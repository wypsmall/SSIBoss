/**
 * Created by neowyp on 2016/8/30.<br>
 * Author   : wangyunpeng<br>
 * Date     : 2016/8/30<br>
 * Time     : 9:47<br>
 * Version  : V1.0<br>
 * Desc     :
 * <br>包含两种模式：状态模式、责任链模式
 * <br>state包下定义状态，将状态解耦出独立处理类
 * <br>exception定义的都是RuntimeException，引入新的处理方式：失败用异常表示
 * <br>handler中定义了责任链处理的接口、抽象、实现类
 * <br>ActionAbs、ConditionAbs是动作与条件的抽象
 * <br>Valid*集成了ConditionAbs表示约束条件
 * <br>*Service集成ActionAbs表示需要做的动作
 * <br>
 * <br>2016-08-30
 * <br>【1】新增两个状态，WaitingAudit、WaitingRefund，通过版本工具比较新增状态增加的代码量
 * <br>增加2个状态，4个方法，特别是修改抽象基类
 * <br>【2】在waitingpay与complete增加中间状态waitingconfirm
 * <br>增加1个状态，1个方法，重点是修改了一个原有状态的方法
 * <br>新增一个中间节点，需要修改原状态的代码，有点像硬编码，需要考虑如何扩展
 * <br>
 * <br>
 * <br>
 */
package com.neo.test.research.partten1;