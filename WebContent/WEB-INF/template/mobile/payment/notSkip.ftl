<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="content-type" content="text/html; charset=UTF-8" />
</head>
<body>
<form id = "pay_form" action="https://101.231.204.80:5000/gateway/api/frontTransReq.do" method="post">
<input type="hidden" name="txnType" id="txnType" value="79"/>
<input type="hidden" name="frontUrl" id="frontUrl" value="http://localhost:8080/zqp/mobile/payment/notify_front.jhtml"/>
<input type="hidden" name="channelType" id="channelType" value="07"/>
<input type="hidden" name="merId" id="merId" value="898320148160325"/>
<input type="hidden" name="tokenPayData" id="tokenPayData" value="{trId=62000000001&tokenType=01}"/>
<input type="hidden" name="txnSubType" id="txnSubType" value="00"/>
<input type="hidden" name="version" id="version" value="5.0.0"/>
<input type="hidden" name="accType" id="accType" value="01"/>
<input type="hidden" name="signMethod" id="signMethod" value="01"/>
<input type="hidden" name="backUrl" id="backUrl" value="http://localhost:8080/zqp/mobile/payment/notify_back.jhtml"/>
<input type="hidden" name="certId" id="certId" value="40220995861346480087409489142384722381"/>
<input type="hidden" name="encoding" id="encoding" value="UTF-8"/>
<input type="hidden" name="bizType" id="bizType" value="000902"/>
<input type="hidden" name="reqReserved" id="reqReserved" value="透传字段"/>
<input type="hidden" name="signature" id="signature" value="hKSBStLHOlcrqE2CqsNP3BSssX5c8rMxoRBO0yNb4ike6cjjEfDz8YdJ4oukZRLpJLS0YRma1gCJGzDVgdBFoEHq/EMwdnUlkv7CYcVI6u6fQ99oQ/x7xmccxSuGFO5pqFbOdRYDElwKeyHYcDnqKziIT76YQBLh1BCDi1qYl4c="/>
<input type="hidden" name="orderId" id="orderId" value="yyaxyybz787878"/>
<input type="hidden" name="txnTime" id="txnTime" value="20151212091225"/>
<input type="hidden" name="accessType" id="accessType" value="0"/>
</form>
</body>

<script type="text/javascript">
	document.all.pay_form.submit();
</script>

</html>