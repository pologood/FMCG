// JavaScript Document

function SetTopMenu(obj)
{
	var menus=obj.parentNode.childNodes;
	for(var i=0; i<menus.length;i++)
	{
		menus[i].className="menutab_out";
	}
	obj.className="menutab_on";
}

function SetRightMenu(obj)
{
    for (var i = 1; i <= 6; i++) {
        var menu = document.getElementById("rm" + i);
		if(menu.className!="rightmenu_bot1"&&menu.className!="rightmenu_bot2")
		{
			menu.className="rightmenu_out";
		}
	}
	
	obj.className="rightmenu_on";

	var index = parseInt(obj.id.substr(2, 1));
	//alert(index);
	if (obj.id == "rm6") {
	    //alert("rm" + (index + 1));
	    document.getElementById("rm" + (index + 1)).className = "rightmenu_bot2";
	    
	}
	else {
	    //alert();
	    
	    //alert(index);
	    document.getElementById("rm"+(index+1)).className = "rightmenu_xj";
		if(document.getElementById("rm7").className == "rightmenu_bot2")
	    {
		    document.getElementById("rm7").className="rightmenu_bot1";
	    }
	}
}