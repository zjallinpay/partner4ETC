$(function () {
    var $step = $("#step");
    $step.step({
        index: 0,
        time: 1000,//点亮时间
        title: ["告示", "余额查询", "兑付申请"]
    });

    $("#nextStep").on("click", function () {
        var index = $step.getIndex();
        if (index == 0) {
            $("#stepTwo").show();
            $("#stepOne").hide();
            $("#stepThree").hide();
            $("#nextStep").show();
        } else if (index == 1) {
            $("#stepTwo").hide();
            $("#stepOne").hide();
            $("#stepThree").show();
            $("#nextStep").hide();
        }
        $step.nextStep();
    });

    $("#customerFile").on("change", function () {
        var fileType = $("#customerFile").val().substr($("#customerFile").val().lastIndexOf(".") + 1);
        if (fileType != "xlsx") {
            layer.alert("只支持xlsx格式文件");
            $("#customerFile").val("");
        }
    });
    // $step.getIndex();// 获取当前的index
    // $step.prevStep();// 上一步
    // $step.nextStep();// 下一步
    // $step.toStep(index);// 跳到指定步骤
});


//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
layui.use(['table', 'element', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;

    //抽取查询方法
    var search = function (data) {
        table.render({
            //表格生成的位置：#ID
            elem: '#repayApplyTable',
            //请求地址
            data: data,
            //是否分页
            page: true,
            //每页展示的条数
            limits: [10, 20, 50],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {field: 'realName', title: '姓名'},
                {field: 'idNo', title: '身份证号'},
                {field: 'cardNo', title: '银行卡号'},
                {field: 'amount', title: '金额(元)'},
                {field: 'remark', title: '备注'}
            ]]
        });
    };

    // form.on('submit(queryFilter)', function (data) {
    //     return false;
    // });

    $("#fileUpload").on("click", function fileUpload() {
        var data = [];
        for (var i = 0; i < 200; i++) {
            var object = {};
            object.realName = "谭光";
            object.idNo = "430224199909099090";
            object.cardNo = "6227009098765678443";
            object.amount = "150000.00";
            object.remark = "卡号姓名不符";
            data.push(object);
        }


        search(data);
    })
});

