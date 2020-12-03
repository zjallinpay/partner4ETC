//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props: {item: {}},
    template: [
        '<li>',
        '	<a v-if="item.type === 0" href="javascript:;">',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span>{{item.name}}</span>',
        '		<i class="fa fa-angle-left pull-right"></i>',
        '	</a>',
        '	<ul v-if="item.type === 0" class="treeview-menu">',
        '		<menu-item :item="item" v-for="item in item.list"></menu-item>',
        '	</ul>',

        '	<a v-if="item.type === 1 && item.parentId === 0" :href="\'#\'+item.url">',
        '		<i v-if="item.icon != null" :class="item.icon"></i>',
        '		<span>{{item.name}}</span>',
        '	</a>',

        '	<a v-if="item.type === 1 && item.parentId != 0" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function () {
    var $content = $('.content');
    $content.height($(this).height() - 154);
    $content.find('iframe').each(function () {
        $(this).height($content.height());
    });
}).resize();

//注册菜单组件
Vue.component('menuItem', menuItem);

var vm = new Vue({
    el: '#rrapp',
    data: {
        user: {},
        menuList: {},
        main: "/manage/main.html",
        password: '',
        newPassword: '',
        navTitle: "控制台"
    },
    methods: {
        getMenuList: function (event) {
            $.ajax({
                url: "/manage/menu/nav?_" + $.now(),
                method: "GET",
                dataType: 'json',
                success: function (r) {
                    if (r.code !== 0) {
                        alert("暂未开通菜单访问权限！");
                    }
                    vm.menuList = r.menuList;
                },
                error: function () {
                    //非200触发
                    window.location.href = "/manage/login"
                }
            });
            // $.getJSON("/manage/menu/nav?_" + $.now(), function (r) {
            //     if (r.code !== 0) {
            //         alert("暂未开通菜单访问权限！");
            //     }
            //     vm.menuList = r.menuList;
            // });

        },

        updatePassword: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['450px', '260px'],
                shadeClose: true,
                shade: 0.05,
                content: jQuery("#passwordLayer"),
                btn: ['修改'],
                btn1: function (index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: "/manage/user/password",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function (index) {
                                    location.reload();
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                }
            });
        }


    },
    created: function () {
        this.getMenuList();
    },
    updated: function () {
        //路由
        var router = new Router();
        routerList(router, vm.menuList);
        router.start();
    }
});


function routerList(router, menuList) {
    for (var key in menuList) {
        var menu = menuList[key];
        if (menu.type == 0) {
            routerList(router, menu.list);
        } else if (menu.type == 1) {
            router.add('#' + menu.url, function () {
                var url = window.location.hash;

                //替换iframe的url
                vm.main = url.replace('#', '');

                //导航菜单展开
                $(".treeview-menu li").removeClass("active");
                $("a[href='" + url + "']").parents("li").addClass("active");

                vm.navTitle = $("a[href='" + url + "']").text();
            });
        }
    }
}
