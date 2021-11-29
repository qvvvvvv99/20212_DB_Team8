<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>ê²ìë¬¼ ìì±</title>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
    <link rel="stylesheet" href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
  <link rel="stylesheet" href="styles/write.css">
  <link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css" rel="stylesheet">
</head>
<body>
  <header>
      <!-- logo -->
      <div class="logo-area">
        <h1 class="logo">
          <a href="index.html">
            <span>LanTravel</span>
            <!-- logo image ì¶ê° í spanì class="hidden" ì¶ê°-->
          </a>
        </h1>
      </div>
      <!-- menu -->
      <nav>
        <ul class="menu">
          <li class="menu-item">
            <a href="login.html"><i class="fas fa-sign-in-alt"></i></a>
          </li>
          <li class="menu-item">
            <a href="#"><i class="fas fa-heart"></i></a>
          </li>
          <li class="menu-item">
            <a href="#"><i class="fas fa-user"></i></a>
          </li>
          <li class="menu-item">
            <a href="upload_post.html"><i class="fas fa-pen-nib"></i></a>
          </li>
        </ul>
      </nav>
    </header>

   <h2>ê²ìë¬¼ ì¬ë¦¬ê¸°</h2>

   <div class="upload-form">
   <form>
   <table class="uploadTable">
    <tbody>
      <tr>
        <td>
        <textarea id="content" cols="80" rows="4" placeholder="ê²ìê¸ì ìë ¥íì¸ì."></textarea>
        </td>
      </tr>
       <tr>
        <td>
        <textarea id="hash" cols="80" rows="1" placeholder="í´ìíê·¸ ìë ¥íì¸ì."></textarea>
        </td>
      </tr>
      <tr>
        <td>
         <input type='file' id='btnAtt' multiple='multiple' />
         <div id='att_zone' data-placeholder='íì¼ì ì²¨ë¶ íë ¤ë©´ íì¼ ì í ë²í¼ì í´ë¦­íê±°ë íì¼ì ëëê·¸ì¤ëë¡­ íì¸ì'></div>
         </div>
        </td>
      </tr> 
      <tr>
         <td>
            ììë ì§ &nbsp
            <input type="date" name="startDate">
            &nbsp &nbsp &nbsp
            ì¢ë£ë ì§ &nbsp
             <input type="date" name="endDate">
         </td>
      </tr>  
      <tr>
         <td>
            êµ­ê°ëª &nbsp
            <input type="text" name="country">
            &nbsp &nbsp &nbsp
            ëìëª &nbsp
             <input type="text" name="city">
              &nbsp &nbsp &nbsp
            ì¥ìëª &nbsp
             <input type="text" name="place" required>
         </td>
      </tr>  
    </tbody>        
   </table>
    <input type="submit" value="ê²ìíê¸°" class="upload-btn">
   </form>
   </div>
      <footer>
      <p>Database(COMP322005) Team8 &copy; 2021</p>
    </footer>
    <script src="scripts/write.js"></script>
</body>
</html>