# 工资单发送工具
使用从模板化Excel导入内容进行邮件发送

## 版本
Version 1.7.3

## 配置说明 
### 邮件信息设置说明： 

|项目|说明|
|---|---|
|标题行|表示表格中标题数据所在的行序号 如：第2行  |
|邮件列|表示表格中邮件地址所在的列序号 如：C列 |
|起始列|表示表格中发送数据开始的列序号 如：A列 |
|结束列|表示表格中发送数据结束的列序号 如：AD列 |

>注意：标题行中若有单元格内容为邮件地址或者纯数字将认 为该标题行非法，无法继续后续操作

### 邮件主题格式说明： 
可以是任意文本，一般设置为{B}-{A1}，表示在B列保存姓名，A1单元格中输入XXXX年XX月工资单

支持的宏如下：

|宏|说明|
|---|---|
|{列序号行序号} |表示表格中的某个单元格数据  如：{A1}表示左上角第一个单元格的内容 |
|{列序号} |表示待发送行中的某列，一般设置为姓名列序号 如：{B} 表示列 B 的内容 |
 
### 发送人信息设置说明： 
 
|项目|说明|
|---|---|
|发送人邮件 |用于发送工资单的邮件地址 |
|邮件密码 |用于发送的邮件的登陆密码 |
|发送服务器 |用于发送的 SMTP 邮件服务器 |
|端口 |用于发送的 SMTP 邮件服务器端口，一般为 25 |
|SSL |用于控制登陆邮件服务器是否需要 SSL |

## 基本操作
- 1、获取Excel模板示例
- 2、依据示例创建清单文件
- 3、选择Excel清单文件
- 4、输入Excel对应配置
- 5、选择【加载人员】
- 6、输入发送相关配置
- 7、选择【发送邮件】

## 其他说明
发送工具下面列表中，红色标识发送失败的记录，绿色表示发送成功的，也可能是上次
已经发送过的。

每次发送后，在各种名单文件一起会创建一个 sendlast 文件，作为日志。

多次发送时候会使用这个日志，防止重复发送。