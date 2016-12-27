## 基于webpack的前端工程化开发

#### 常用命令

执行`npm install`安装项目所需依赖
执行`npm run serve`启动devServer
执行`npm run dist`发布文件

#### 主要目录结构介绍

```
- website
    - src                #代码开发目录
        - css            #css目录，按照页面（模块）、通用、第三方三个级别进行组织
            + page
            + common
            + lib
        + img            #图片资源
        - js             #JS脚本，按照page、components进行组织
            + page
            + components
        + pages           #HTML模板
    - dist               #webpack编译打包输出目录，无需建立目录可由webpack根据配置自动生成
        + css                
        + js
        + pages
    + node_modules       #所使用的nodejs模块
    package.json         #项目配置
    webpack.config.js    #webpack配置
    README.md            #项目说明
    .babelrc             #babel配置
    .editorconfig        #编辑器设置
    .eslintrc            #eslint设置
    .gitignore           #git忽略文件
```

#### 开发页面

在src/js/page目录下建立js文件，在src/pages目录下建立html文件。入口js和模板文件名对应。


##### pages

1. index
2. search
3. article
4. chart
5. sub
6. user
7. help
8. about

##### components
footer
header
pubsub