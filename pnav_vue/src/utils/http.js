/**
 * 跳转到支付界面 - 支付宝
 */
function toPay(payParam) {
  const div = document.createElement("div");
  div.innerHTML = payParam;
  document.body.appendChild(div);
  document.forms[0].submit();
}

exports.toPay = toPay;
