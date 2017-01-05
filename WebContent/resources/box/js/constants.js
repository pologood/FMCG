/*
 * constants.js 常量类
 * darell.wu 20120706 10:59
 */
var constants = (function() {
	
	var innerSTATUS = {
		YES : 'Y',
		NO : 'N'
	};
	
	/**
	 * 节点类型
	 */
	var nodeType = {
		ORGAN : 'O',//组织机构
		AGENT : 'A'//监控节点
	};
	
	return {
		STATUS : innerSTATUS,
		NODETYPE : nodeType
	};
	
})();