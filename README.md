- [JetpackComposeMVI基础架构项目](#jetpackcomposemvi基础架构项目)
  - [介绍](#介绍)
  - [项目版本](#项目版本)
  - [技术栈](#技术栈)
    - [UI库](#ui库)
    - [存储库](#存储库)
    - [网络库](#网络库)
    - [其他库](#其他库)
  - [分包管理](#分包管理)
    - [包树形结构](#包树形结构)
    - [包介绍](#包介绍)
  - [view层的命名习惯（个人习惯，经供参考）](#view层的命名习惯个人习惯经供参考)
  - [网络层的封装](#网络层的封装)
    - [网络构建者：RequestBuilder.kt](#网络构建者requestbuilderkt)
    - [使用示例](#使用示例)


# JetpackComposeMVI基础架构项目

## 介绍

>这是一个MVI的Jetpack Compose Material3的基础项目，其中封装好了网络层，添加了一个简单的登录，存储，页面跳转的示例，使用Navigation进行页面的导航，遵循着MVI架构的规范对页面中的显示数据放在State中，交互相关的操作在Intent里，View通过对ViewModel发送Intent来修改状态从而响应View的变化，实现单向数据流的数据操作流程，使用hilt框架进行网络请求工具等注入，网络框架对retrofit2进行了简单的封装，使用flow进行网络请求流程的监听。其中用户的登录信息用DataStorePreferences进行存储。简单的信息，比如是否第一次进入软件，是否保存和自动登录等简单安全级别不高的状态值，使用mmkv进行存储。本项目没有过多的进行分包和封装，只内置了常用的一些业务逻辑，也可以作为MVI架构的参考来进行修改，主要是为减少学习成本，不用拿到一个架构就看各种别人自定义的封装内容。这是一个简单但是不简陋的基础项目。

## 项目版本

- AGP：8.1.0
- Kotlin：1.8.10
- CompileSdk 33
- MinSdk 26

## 技术栈

### UI库

-  JetpackComposeMaterial3

### 存储库

-  MMKV

- DataStore-Preferences

### 网络库

- Retrofit2
- Cookie

### 其他库

- Hilt
- Navigation
- Icon+
- Systemuicontroller


## 分包管理

### 包树形结构

```
├─activity
├─config
├─constant
├─di
├─model
│  ├─body
│  └─state
├─network
│  ├─api
│  └─res
├─ui
│  ├─components
│  ├─screen
│  │  ├─detail
│  │  ├─guide
│  │  ├─login
│  │  └─main
│  │      ├─home
│  │      └─user
│  └─theme
└─util
```



### 包介绍

- activity：存放Activity内容，单activity的时候主要存放MainActivity
- config：存放DataStore，MMKV初始化，APP路由配置等
- constant：存放一些常量，例如网络请求的请求超时时间设定等
- di：存放Hilt框架的一些注入模块，比如RequestBuilder，DataStore等
- model：存放一些公共会使用到的类结构，比如请求体，公共的状态
- network：存在网络层的主要内容，例如响应映射类，api接口，网络构建者工具
- ui：主要存放view的内容，其中的components存放公共的一些组件组合函数，screen存放每个页面对应的view内容，theme存放app的主题，配色，字体等
- util：存放工具类

## view层的命名习惯（个人习惯，仅供参考）

例如Login页面（.ui.screen.login）

LoginView：存放view的ui组合函数，主要是通过调用LoginView来进行Login页面的显示

LoginViewComponents：Login页面自己的自己部分ui组件

LoginViewIntent：存放LoginViewModel和相关的Intent

LoginViewState：存放Login页面上的数据相关状态，例如登录账号密码，是否记住密码，是否自动登录状态等

## 网络层的封装

### 网络构建者：RequestBuilder.kt

> 建议使用：通过hilt创建RequestBuilder模块，在ViewModel进行注入使用

RequestBuilder是网络工具的核心，使用getAPI进行APi接口的创建，然后调用对应的API获取对应execute传入给getResponse，getResponse会返回一个flow然后监听flow的数据流，其中会返回三种状态，请求开始Loding，请求成功，请求失败。然后进行对应的操作，要在线程中进行请求，所以建议在ViewModel中进行使用，使用官方的*viewModelScope*.launch{}创建协程作用域进行网络请求

### 使用示例

> LoginViewIntent中的LoginViewModel有登录的请求示例(已注释)

```kotlin
//LoginViewIntent.kt
//已在LoginViewModel的主构造函数中进行了注入
//协程中调用的请求
requestBuilder.apply {
    //调用getResponse监听返回的flow
    getResponse {
        //调用getAPI传入API接口类型，并且调用函数，然后调用execute转为同步请求
        getAPI(UserAPI::class).login().execute()
    }.collect {//监听流发送出来的数据
        when (it) {//提交到流中的数据是一个密封类，可以直接用when进行判断类型
            is RequestStatus.Error -> {//请求失败
                Log.e("TAG", "loginRequest:${it.errMsg} ${it.exception} ", )
            }
            RequestStatus.Loading -> {}//正在请求
            is RequestStatus.Success -> {//网络请求成功
            }
        }
    }
}

//RequestBuilder.kt
//请求会提交给flow的三种状态
sealed class RequestStatus<out T> {
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Error(val exception: Exception, val errMsg: String) : RequestStatus<Nothing>()
    object Loading : RequestStatus<Nothing>()
}
```



