/**
 * 微信相关<br>
 * 1.所有rest API不能使用其他模块,可以调用公共模块的controller和其他相关模块的service<br>
 * 2.rest API以/zy/wx/{s|t}/{模块}/{接口功能},界面层面的家长其实还是学生~,所以不存在家长<br>
 * 3.仅仅跟微信相关的service写到此模块的api包下面,其他公共的service接口写到yooShare-service的zuoye模块下
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月28日
 */
package com.lanking.uxb.service.wx;