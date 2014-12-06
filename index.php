<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<style>
body  {
    background-image: url("background.png");
	background-repeat:no-repeat;
	
    background-color: #cccccc;
}

   .image{
    position: absolute;
    left: 480px;
    top: 340px;
    z-index: 1;

}
   .progress{
    font-family: Georgia;
    color: #000099;
    font-size: 30px;
    position: absolute;
    left: 480px;
    top: 300px;
    z-index: 2;

}
   .tg {
    border-collapse:collapse;border-spacing:0;border-color:#bbb;
    position: absolute;
    left: 480px;
    top: 340px;
    z-index: 2;
}
   .tg td{
font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#bbb;color:#594F4F;background-color:#E0FFEB;

}

   .tg th{
font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#bbb;color:#493F3F;background-color:#9DE0AD;
}
    .textbox {
    height: 25px;
    width: 275px;
    border: 1px solid #B9BDC1;
    color: #797979;
    -moz-box-shadow: 0 2px 4px #bbb inset;
    -webkit-box-shadow: 0 2px 4px #BBB inset;
    box-shadow: 0 2px 4px #BBB inset;
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    border-radius: 3px;
	position: absolute;
    left: 480px;
    top: 220px;
    z-index: 1;
}

.textbox:focus {
    background-color: #E7E8E7;
    outline: 0;
}

.btn {
  background: #3498db;
  background-image: -webkit-linear-gradient(top, #3498db, #2980b9);
  background-image: -moz-linear-gradient(top, #3498db, #2980b9);
  background-image: -ms-linear-gradient(top, #3498db, #2980b9);
  background-image: -o-linear-gradient(top, #3498db, #2980b9);
  background-image: linear-gradient(to bottom, #3498db, #2980b9);
  -webkit-border-radius: 28;
  -moz-border-radius: 28;
  border-radius: 28px;
  font-family: Georgia;
  color: #ffffff;
  font-size: 12px;
  padding: 7px 16px 7px 16px;
  text-decoration: none;
  	position: absolute;
  left: 780px;
  top: 217px;
  z-index: 1;
}

.btn:hover {
  background: #3cb0fd;
  background-image: -webkit-linear-gradient(top, #3cb0fd, #3498db);
  background-image: -moz-linear-gradient(top, #3cb0fd, #3498db);
  background-image: -ms-linear-gradient(top, #3cb0fd, #3498db);
  background-image: -o-linear-gradient(top, #3cb0fd, #3498db);
  background-image: linear-gradient(to bottom, #3cb0fd, #3498db);
  text-decoration: none;
}


</style>
<script type="text/javascript"> 
function status(){
    var img = document.createElement("IMG");

    img.src = "searching.gif";

    document.getElementById('imageDiv').appendChild(img);

}
</script>

</head>

<body>
<?php
if (isset($_POST['txt'])){
function printme($txt){
$output=shell_exec('sudo su - -c "hdfs dfs -rm -r /output*" jagadish');
$output=shell_exec('sudo su - -c "hadoop jar pagerank.jar Driver /input /output 0.15" jagadish');
$output=shell_exec('sudo su - -c "hdfs dfs -cat /outputsearchresult/part-r-00000" jagadish');
echo '<div class="progress">' , "Search Result" , '</div>';
//#echo $output;
//$output="111 23 346 567 87 8787";
$result = explode("\n",$output);
echo '<table class="tg">';
echo '<tr><th class="tg-031e">NodeId</th><th class="tg-031e">Data</th></tr>';
foreach ($result as $value){
echo '<tr>';
$valueArray = explode("\t",$value);
echo '<td class="tg-031e">'.$valueArray[0].'</td>'; 
echo '<td class="tg-031e">'.$valueArray[1].'</td>';
echo '</tr>';
}
//echo '</tr>';
echo '</table>';
//'</div>';
}
printme("{$_POST['txt']}");
}
?>
<form action="<?php $_PHP_SELF ?>" method="POST">
<input class="textbox" type="text" name="txt" />
<input class="btn" type="submit" value="search" onclick="status()"/>
</form>
<div id="imageDiv" class="image"></div>
</body>
</html>
