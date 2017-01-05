<header class="main-header">
    <!-- Logo -->
    <a href="${base}/store/member/index.jhtml" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>店</b></span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><img src="${base}/resources/store/images/LOGO.png" style="margin: 0 auto;" width="100px" alt=""></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- Messages: style can be found in dropdown.less-->
                <li class="dropdown messages-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-envelope-o"></i>
                        <span class="label label-success">
                        [#if findMessageList??&&findMessageList?has_content]${findMessageList.size()}[#else]0[/#if]</span>
                    </a>
                    <ul class="dropdown-menu" style="width:400px;">
                        <li class="header i-blue">
                            您有[#if findMessageList??&&findMessageList?has_content]${findMessageList.size()}[#else]0[/#if]条信息
                            [#if findMessageList??&&findMessageList?has_content]
                                <input type="button" value="全部标记为已读" class="btn btn-primary btn-sm" style="margin-left: 175px;" onclick="read_all()">
                            [/#if]
                        </li>
                    [#if findMessageList??&&findMessageList?has_content]
                        
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                [#list findMessageList as list]
                                    <li><!-- start message -->
                                        <a href="#">
                                            <div>
                                                <div>
                                                    <i class="fa fa-bell-o"></i> ${list.title}
                                                    <small style="margin-left:5px;">
                                                        <i class="fa fa-clock-o"></i> ${list.createDate?string("yyyy-MM-dd HH:mm:ss")} 
                                                        <input type="button" value="标记为已读" class="btn btn-primary btn-sm" style="margin-left:75px;" onclick="read(${list.id})">
                                                    </small>
                                                    <p>${list.content}</p>
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                [/#list]

                            </ul>
                        </li>
                        <li class="footer i-blue">
                            <a href="${base}/store/member/message/list.jhtml">查看所有信息</a>
                        </li>
                    [/#if]
                    </ul>
                </li>
                <!-- Notifications: style can be found in dropdown.less -->
                <li class="dropdown messages-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-bell-o"></i>
                        <span class="label label-success" id="msg_num_head">[#if findMsgOrderList??&&findMsgOrderList?has_content]${findMsgOrderList.size()}[#else]0[/#if]</span>
                    </a>
                    <ul class="dropdown-menu" style="width:400px;">
                        <li class="header i-blue">
                            您有<span id="msg_num_body">[#if findMsgOrderList??&&findMsgOrderList?has_content]${findMsgOrderList.size()}[#else]0[/#if]</span>条信息
                            [#if findMsgOrderList??&&findMsgOrderList?has_content]
                                <input type="button" value="全部标记为已读" class="btn btn-primary btn-sm" style="margin-left: 175px;" onclick="read_all()">
                            [/#if]
                        </li>
                    [#if findMsgOrderList??&&findMsgOrderList?has_content]
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                [#list findMsgOrderList as list]
                                    <li><!-- start message -->
                                        <a href="#">
                                            <div>
                                                <div>
                                                    <i class="fa fa-bell-o"></i> ${list.title}
                                                    <small style="margin-left:5px;">
                                                        <i class="fa fa-clock-o"></i> ${list.createDate?string("yyyy-MM-dd HH:mm:ss")} 
                                                        <input type="button" value="标记为已读" class="btn btn-primary btn-sm" style="margin-left:75px;" onclick="read(${list.id})">
                                                    </small>
                                                    <p>${list.content}</p>
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                [/#list]

                            </ul>
                        </li>
                        <li class="footer i-blue">
                            <a href="${base}/store/member/message/list.jhtml">查看所有信息</a>
                        </li>
                    [/#if]
                    </ul>
                </li>
                <script type="text/javascript">
                    function read(id) {
                        if (window.confirm("确认标记已读吗？")) {
                            $.ajax({
                                url: "${base}/store/member/message/read.jhtml",
                                type: "post",
                                data: {id: id},
                                dataType: "json",
                                success: function (data) {
                                    if (data.type == "success") {
                                        $.ajax({
                                            url: "${base}/store/member/get_msg_num.jhtml",
                                            type: "get",
                                            dataType: "json",
                                            success: function (msg) {
                                                $.message(data);
                                                if (msg != "") {
                                                    $("#msg_num_head").html(msg.msg_num);
                                                    $("#msg_num_body").html(msg.msg_num);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                    function read_all() {
                        if (window.confirm("确认要全部标记为已读吗？")) {
                            $.ajax({
                                url: "${base}/store/member/message/read_all.jhtml.jhtml",
                                type: "post",
                                dataType: "json",
                                success: function (data) {
                                    $.message(data);
                                    if (data.type == "success") {
                                        $.ajax({
                                            url: "${base}/store/member/get_msg_num.jhtml",
                                            type: "get",
                                            dataType: "json",
                                            success: function (msg) {
                                                $.message(data);
                                                if (msg != "") {
                                                    $("#msg_num_head").html(msg.msg_num);
                                                    $("#msg_num_body").html(msg.msg_num);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                </script>
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="[#if owner??]${owner.tenant.logo}[#else]${base}/resources/store/2.0/dist/img/user2-160x160.jpg[/#if]" class="user-image" alt="User Image">
                        <span class="hidden-xs">[#if owner??]${owner.tenant.name}[#else][/#if]</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header">
                            <img src="[#if owner??]${owner.tenant.logo}[#else]${base}/resources/store/2.0/dist/img/user2-160x160.jpg[/#if]" class="img-circle" alt="User Image">
                            <p>
                            [#if owner??]${owner.tenant.name}[#else][/#if]
                                <small>[#if owner??]${owner.tenant.telephone}[#else][/#if]</small>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <li class="user-body">
                            <div class="row">
                                <div class="col-xs-4 text-center">
                                    <a href="javascript:;">余额</a>
                                    <div>[#if owner??]${owner.balance?string("0.00")}[#else]0.00[/#if]</div>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="javascript:;">冻结金额</a>
                                    <div>[#if owner??]${owner.freezeBalance?string("0.00")}[#else]0.00[/#if]</div>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="javascript:;">库存金额</a>
                                    <div>[#if StockAmount??]${StockAmount?string("0.00")}[#else]0.00[/#if]</div>
                                </div>
                            </div>
                            <!-- /.row -->
                        </li>
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="${base}/store/member/deposit/fill.jhtml" class="btn btn-primary btn-flat">充值</a>
                                <a href="${base}/store/member/cash/index.jhtml" class="btn btn-primary btn-flat">提现</a>
                            </div>
                            <div class="pull-right">
                                <a href="/store/member/logout.jhtml" class="btn btn-danger btn-flat">退出</a>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>