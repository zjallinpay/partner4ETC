//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到


layui.use(['table', 'element', 'laypage', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;

    var statusTpl = function (d) { // 参数d是当前行数据
        if (d.status == 1) {
            return '<input type="checkbox" lay-skin="switch" lay-text="启用|禁用" checked> ';
        } else {
            return '<input type="checkbox" lay-skin="switch" lay-text="启用|禁用"> ';
        }
    };

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#userTable',
            //请求地址
            url: '/sys/user/list',
            //是否分页
            page: true,
            toolbar: '#toolbarDemo',
            cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            //是否分页
            ,
            //请求参数
            where: {
                username: $("#username").val()
                // roletype: $("#roletype").val()
            },
            //分页信息
            request: {
                pageName: 'pageNo',
                limitName: 'pageSize'
            },
            //设置返回的属性值，依据此值进行解析
            response: {
                statusName: 'code',
                statusCode: 0,
                msgName: 'msg',
                dataName: 'data'
            },
            //每页展示的条数
            limits: [10, 20, 30],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {type: 'checkbox', fixed: 'left'},
                // {field: 'id', width: 80, title: 'ID', sort: true},
                {field: 'userId', width: 80, title: '用户ID', sort: true},
                {field: 'username', width: 110, title: '用户名'},
                {field: 'password', width: 120, title: '密码', sort: true},
                {field: 'status', width: 100, title: '状态', templet: statusTpl},
                {field: 'roleName', width: 100, title: '角色', sort: true},
                {field: 'createTime', width: 170, title: '创建时间', sort: true},
                {field: 'updateTime', width: 170, title: '修改时间'},
                {field: 'lastLoginTime', width: 130, title: '最后登录时间', sort: true},
                {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 130}
            ]]
        });
    };

    //条件查询
    $("#queryBtn").on("click", function () {
        table.reload('userTable', {
            url: "/sys/user/list"
            , where: {
                username: $("#username").val()
            }
            , page: {
                curr: 1
            }

        });

    });

    //页面加载就查询列表
    search();

    /*//打开添加页面模态框
    $("#addBtn").on("click", function () {
        layer.open({
            type: 2,
            title: '添加用户',
            shadeClose: true,
            shade: 0.8,
            area: ['500px', '65%'],
            content: 'addUser11.html', //iframe的url
            btn: ['确认', '取消'],
        });

    });


    //重置参数
    $("#resetBtn").on("click", function () {
        //alert("resetBtn")
        $("#username").val("");
        // layer.alert("参数清除");
    });


    //进行编辑操作
    $("#editSubmit").on("click", function () {
        $.ajax({
            url: '/web/editById',
            type: 'post',
            data: {
                id: '001'
            },
            // async: false,
            dataType: 'json',
            success: function (data) {
                if (data.code == "00000") {
                    var index = layer.alert("编辑成功", function () {
                        //点击确认按钮执行回调函数
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert("编辑失败！");
                }
            },
            error: function () {
                layer.alert("编辑失败，请重试！");
            }
        });
    });


    $("#addSubmit").on("click", function () {
        $.ajax({
            url: '/web/add',
            type: 'post',
            data: {
                id: '001'
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == "00000") {
                    var index = layer.alert("添加成功", function () {
                        //点击确认按钮执行回调函数
                        layer.closeAll();
                        search();
                    });
                } else {
                    layer.alert("添加失败！");
                }
            },
            error: function () {
                layer.alert("添加失败，请重试！");
            }
        })
    });


    //监听form表单提交事件 防止页面跳转
    form.on('submit(editFilter)', function (data) {
        return false;
    });

    form.on('submit(addFilter)', function (data) {
        return false;
    });

    //打开模态框
    function openModal(operateName, modalName) {
        layer.open({
            title: operateName,
            content: $('#' + modalName),
            area: ['700px', '450px'],
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1
        });
    }
*/

    //监听行工具事件
    table.on('tool(userTable)', function (obj) {
        var data = obj.data;
        //console.log(obj)
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                obj.del();
                table.reload("userTable", {
                    url: '/sys/user/del',
                    where: {
                        "id": data.userId
                    },
                    success: function () {
                        search();
                        layer.closeAll();
                        // location.href = index.html;
                    }
                });
                layer.close(index);
                search();
            });
        } else if (obj.event === 'edit_selectrole') {
            layer.open({
                type: 2,
                title: '编辑用户',
                shadeClose: true,
                shade: 0.8,
                area: ['500px', '65%'],
                content: '/manage/user/update?opreate=edit&userId=' + data.userId+'&roleIds='+data.roleId, //iframe的url
                btn: ['关闭'],
                yes: function () {
                    search();
                    layer.closeAll();
                }

            });
        }
    });


    //头工具栏事件
    table.on('toolbar(userTable)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
        switch (obj.event) {
            case 'getCheckData':
                var data = checkStatus.data;  //获取选中行数据
                layer.alert(JSON.stringify(data));
                break;
            case 'queryBtn':
                search();
                break;
            case 'resetBtn':
                $("#username").val("");
                break;
            case 'addBtn':
                layer.open({
                    type: 2,
                    title: '添加用户',
                    shadeClose: true,
                    shade: 0.8,
                    area: ['500px', '65%'],
                    content: 'user/add', //iframe的url
                    btn: ['关闭'],
                    yes: function () {
                        search();
                        layer.closeAll();
                    }

                });
                break;
        }
        ;
    });


    //触发事件
    var active = {
        setTop: function () {
            var that = this;
            layer.open({
                type: 2,
                title: '添加用户',
                shadeClose: true,
                shade: 0.8,
                area: ['500px', '80%'],
                content: 'user/add', //iframe的url
                btn: ['关闭'],
                yes: function () {
                    search();
                    layer.closeAll();
                }

            });


        }
    };

    $('#layerDemo .layui-btn').on('click', function () {
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });

});