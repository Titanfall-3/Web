function login() {

  var loginNameElement = getElementById('loginName');
  var loginPasswordElement = getElementById('loginPassword');

  var xhr = new XMLHttpRequest();
  var url = "url";
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var json = JSON.parse(xhr.responseText);
      console.log(json.email + ", " + json.password);
    }
  };
    var data = JSON.stringify({"username": loginNameElement.value, "password": loginPasswordElement.value});
    xhr.send(data);
}
