## pnav
A personal navigator.

我们都有这样的困扰，在多台电脑（比如在家里一部、在办公室一部、网吧公共电脑）之间，浏览器网页标签没法共享。
或许想用谷歌插件，但是网络不一定连得上，且有时在公共电脑上不愿意登录个人账号。
即使同一台电脑，不同浏览器之间，也难以同步书签。

本项目就是用来解决这个问题的。简单说，就是一个属于个人的个性化导航页。

## note
- 开发时，记得将pnav_vue里的.gitignore的内容复制到根目录下的.gitignore里，并加上pnav_vue/前缀
- vue.config.js里的productionSourceMap: false，设为true时会报错，不知道为什么？


## stacks

- django: 用于后台管理系统和接口
- mysql: 用于存储数据
- celery + redis: 其实没必要，为了学习而用
- vue.js: 前端，其实简单的html也行，为了学习而用
- html + js + css: 基本技术栈
- nginx: 用于配置域名映射

## dev-plan

-[x] vue.js简单的前端展示，静态页面
-[ ] django admin后台管理系统
-[ ] 绑定域名
-[ ] django添加标签页
-[ ] django后台接口: 返回标签内容
-[ ] vue从后台获取标签内容并渲染
-[ ] 美化样式
-[ ] other
