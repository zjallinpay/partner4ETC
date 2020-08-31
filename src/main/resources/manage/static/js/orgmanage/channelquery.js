//element 展示左边菜单栏; 预加载需要使用的模块
//由于layer弹层依赖jQuery，所以可以直接得到
var layer=null;
layui.use(['table', 'element', 'laypage', 'layer', 'form'], function () {
    var table = layui.table;
    var $ = layui.$;
    layer = layui.layer;
    var form = layui.form;

    //初始化开始查询时间
    // var inittime = getNowFormatDate();
    // $("#creatdate").val(inittime);
    // $("#modifydate").val(inittime);

    //抽取查询方法
    var search = function () {
        table.render({
            //表格生成的位置：#ID
            elem: '#orginfotable',
            //请求地址getList
            url: '/manage/query/org/getList',
            //是否分页
            page: true,
            //请求参数
            where: {
                partnerId: $("#chanelid").val(),//机构编号
                partnerName: $("#chanelname").val(),//机构名称
                partnerType: $("#chanetype").val(),//机构类型
                sbstatus: "('1','2','3','4','5','6')",//默认查询状态
                status: $("#chanelstatus").val(),//机构状态
                // createTime:new Date($("#creatdate").val().split(" ")[0].split('-')[0], $("#creatdate").val().split(" ")[0].split('-')[1]-1, $("#creatdate").val().split(" ")[0].split('-')[2]),
                // modifyTime:new Date($("#modifydate").val().split(" ")[0].split('-')[0], $("#modifydate").val().split(" ")[0].split('-')[1]-1, $("#modifydate").val().split(" ")[0].split('-')[2])

                // createTimeStart:"2019-07-03",
                createTimeStart: $("#creatdate").val()==""?"":$("#creatdate").val().substr(0,10),//查询创建时间起
                createTimeEnd:$("#creatdate").val()==""?"":$("#creatdate").val().substr(12,11),//查询创建时间止
                modifyTimeStart: $("#modifydate").val() == "" ? "" : $("#modifydate").val().substr(0, 10),//更新时间起
                modifyTimeEnd: $("#modifydate").val() == "" ? "" : $("#modifydate").val().substr(12, 11)//更新时间止
            },
            //分页信息
            request: {
                pageName: 'pageNum',
                limitName: 'pageSize'
            },

            //处理返回参数
            parseData: function (res) {

                if(res.data.total == 0) {
                            var index = layer.alert("无数据，请修改查询条件", function () {
                                layer.close(index);
                            })
                            return {
                                "code": res.code,
                                "msg": "无数据",
                                "count": res.data.total,
                                "data": res.data.list
                            };
                    }
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.data.total,
                    "data": res.data.list
                };
            },

            //设置返回的属性值，依据此值进行解析
            // response: {
            //     statusName: 'code',
            //     statusCode: '0000',
            //     msgName: 'msg',
            //     dataName: 'data'
            // },

            //每页展示的条数
            limits: [5,10,20],
            //每页默认显示的数量
            limit: 10,
            //单元格设置
            cols: [[
                {
                    field: 'partnerId',
                    width: 120,
                    title: '机构编号',
                    event: 'setSign',
                    style: 'color: #024CA1;font-style:italic;text-decoration:underline'
                },
                {field: 'partnerName', title: '机构名称'},
                {field: 'partnerType', title: '机构类型'},
                {field: 'secretKey',  title: '机构秘钥'},
                {field: 'partnerAddress',  title: '机构地址', hide: true},
                {field: 'saler',  title: '推广人', hide: true},
                {field: 'legalName',  title: '法人姓名', hide: true},
                {field: 'legalId',  title: '法人身份证', hide: true},
                {field: 'legalPhone',  title: '法人联系方式', hide: true},
                {field: 'contactor', title: '机构联系人', hide: true},
                {field: 'contactPhone',  title: '联系人电话', hide: true},
                {field: 'url',  title: '请求服务地址', hide: true},

                {field: 'license',  title: '身份证证明', hide: true},
                {field: 'idFront',  title: '身份证证明', hide: true},
                {field: 'idBack',  title: '身份证反面', hide: true},
                {field: 'agreement',  title: '协议图片', hide: true},


                {field: 'parentId',  title: '父机构编号'},
                {field: 'businessLicenceNo',  title: '营业执照编号'},
                {field: 'parstatus',  title: '机构状态'},
                {field: 'failReason', title: '审核意见'},
                {field: 'createTimeX',  title: '创建时间'},
                {field: 'modifyTimeX', title: '更新时间'},
                {field: 'sysUser', title: '最后操作人'}
            ]],
            done: function (res, curr, count) {

                $("[data-field='partnerType']").children().each(function () {
                    if ($(this).text() == '00') {
                        $(this).text("银行")
                    } else if ($(this).text() == '01') {
                        $(this).text("汽车服务")
                    } else if ($(this).text() == '02') {
                        $(this).text("互联网平台")
                    } else if ($(this).text() == '03') {
                        $(this).text("其他")
                    }
                });
            }
        });

        //监听行单击事件（单击事件为：rowDouble）
        // table.on('row(test)', function(obj){
        //     var data = obj.data;
        //
        //     // layer.alert(JSON.stringify(), {
        //     //     title: '当前行数据：'
        //     // });
        //     showinfo(data);
        //     //标注选中样式
        //     obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
        // });

    };

    layui.use('table', function () {
        var table = layui.table;
        //监听单元格事件
        table.on('tool(demoEvent)', function (obj) {
            var data = obj.data;
            if (obj.event === 'setSign') {
                showinfo(data);
            }
        });
    });


    //页面加载就查询列表
    search();

    //条件查询
    $("#queryBtn").on("click", function () {
        // var index = layer.alert("立即提交", function () {
        //     layer.close(index);
            search();
        // })

    });

    $("#bck").on("click", function () {
        layer.closeAll();
    });

    $("#agreementBtn").on("click", function () {
        window.open($("#pdfUrl").val());
    });

    function showinfo(data){

        $("#chanelidshow").val(data.partnerId);//机构编号
        $("#chanelnameshow").val(data.partnerName);//机构名称
        $("#chaneltypeshow").val(partnerTypere(data.partnerType));//机构类型
        $("#partneridshow").val(data.parentId);//父机构编号
        $("#registidshow").val(data.businessLicenceNo);//营业执照编号
        $("#chaneladdressshow").val(data.partnerAddress);//机构地址
        $("#leglnameshow").val(data.legalName);//法人姓名
        $("#legldcardshow").val(data.legalId);//法人证件号
        $("#leglphoneshow").val(data.legalPhone);//法人身份证号
        $("#chanelpersonshow").val(data.contactor);//机构联系人
        $("#chanelpersonphoneshow").val(data.contactPhone);//联系人电话
        $("#chanelstatusshow").val(data.parstatus);//机构状态
        $("#creattimeshow").val(data.createTimeX);//创建时间
        $("#modifytimeshow").val(data.modifyTimeX);//更新时间
        $("#approvalopinionshow").val(data.failReason);//审批意见
        $("#secretkeyshow").val(data.secretKey);//秘钥

        // $("#front").attr("src","http://10.48.1.8:8080/query/getImg?partnerId="+data.partnerId+"&imgid="+data.idFront);
        // $("#back").attr("src","http://10.48.1.8:8080/query/getImg?partnerId="+data.partnerId+"&imgid="+data.idBack);
        // $("#aggre").attr("src","http://10.48.1.8:8080/query/getImg?partnerId="+data.partnerId+"&imgid="+data.agreement);
        $('#license').attr('src', "/manage/etcimg/org/" + data.partnerId + "/license/" + data.license);
        $('#front').attr('src', "/manage/etcimg/org/" + data.partnerId + "/front/" + data.idFront);
        $("#back").attr("src", "/manage/etcimg/org/" + data.partnerId + "/back/" + data.idBack);
        $("#pdfUrl").val("/manage/etcimg/org/" + data.partnerId + "/agreement/" + data.agreement);

        //打开模态框
        openModal("详细信息", "editForm");
    }


    function partnerTypere(type){
        if(type =='00'){
            return  "银行";
        }
        if(type =='01'){
            return  "汽车服务";
        }
        if(type =='02'){
            return  "互联网平台";
        }
        if(type =='03'){
            return  "其他";
        }
    }
    //打开模态框
    function openModal(operateName, modalName) {
        layer.open({
            title: operateName,
            content: $('#' + modalName),
            area: '700px',
            //点击遮罩关闭窗口
            shadeClose: true,
            //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            type: 1,
            moveOut: true
        });
    }

    //监听form表单提交事件 防止页面跳转
    form.on('submit(backbtn)', function (data) {
        return false;
    });

    form.on('submit(addFilter)', function (data) {
        return false;
    });

    /*图片点击事件*/
    $(".pimg").click(function(){
        var _this = $(this);//将当前的pimg元素作为_this传入函数
        imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
    });

    function imgShow(outerdiv, innerdiv, bigimg, _this){
        var src = _this.attr("src");//获取当前点击的pimg元素中的src属性
        $("#bigimg").attr("src", src);//设置#bigimg元素的src属性

        /*获取当前点击图片的真实大小，并显示弹出层及大图*/
        $("<img/>").attr("src", src).load(function(){
            var windowW = $(window).width();//获取当前窗口宽度
            var windowH = $(window).height();//获取当前窗口高度
            var realWidth = this.width;//获取图片真实宽度
            var realHeight = this.height;//获取图片真实高度
            var imgWidth, imgHeight;
            var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

            if(realHeight>windowH*scale) {//判断图片高度
                imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
                imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
                if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度
                    imgWidth = windowW*scale;//再对宽度进行缩放
                }
            } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度
                imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放
                imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度
            } else {//如果图片真实高度和宽度都符合要求，高宽不变
                imgWidth = realWidth;
                imgHeight = realHeight;
            }
            $(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放

            var w = (windowW-imgWidth)/2;//计算图片与窗口左边距
            var h = (windowH-imgHeight)/2;//计算图片与窗口上边距
            $(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性
            $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
        });

        $(outerdiv).click(function(){//再次点击淡出消失弹出层
            $(this).fadeOut("fast");
        });
    }

});

function query(){
    var index = layer.alert("立即提交", function () {
        layer.close(index);
        search();
    })
}

function back(){
    //点击确认按钮执行回调函数
    layer.closeAll();
}
