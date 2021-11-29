<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8">
    <title>íì ê°ì</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
    <link rel="stylesheet" href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
    <link rel="stylesheet" href="styles/register.css">
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
   <div class="register-form">
      <h1>íì ê°ìì íìí©ëë¤</h1>
      <form>
        <fieldset>
          <legend>ì¬ì©ì ì ë³´</legend>    
          <ul>
            <li>
              <label class="reg" for="uid">ìì´ë <em> * </em></label>
              <input type="text" id="uid" class="text-field" autofocus placeholder="4ì ~ 10ì ì¬ì´, ê³µë°±ìì´" required> 
            </li>
            <li>
              <label class="reg" for="pwd1">ë¹ë°ë²í¸ <em> * </em></label>
              <input type="password" id="pwd1" class="text-field" placeholder="ë¬¸ìì ì«ì, í¹ì ê¸°í¸ í¬í¨" required> 
            </li>        
            <li>
              <label class="reg" for="pw2">ë¹ë°ë²í¸ íì¸ <em> * </em></label>
              <input type="password" id="pwd2" class="text-field" required> 
            </li>
            <li>
              <label class="reg" for="umail">ì´ë©ì¼ <em> * </em></label>
              <input type="email" id="umail" class="text-field" required> 
            </li>
            <li>
              <label class="reg" for="alias">ëë¤ì <em> * </em></label>
              <input type="text" id="alias" class="text-field" required> 
            </li>
            <li>
              <label class="reg" for="tel">ì íë²í¸</label>
              <input type="tel" id="tel" class="text-field"> 
            </li>
          </ul>      
        </fieldset>
        <div>
          <input type="submit" value="ê°ìíê¸°" class="reg-btn">
          <input type="reset" value="ì´ê¸°í" class="reg-btn">
        </div>
      </form> 
      </div>   
            <footer>
      <p>Database(COMP322005) Team8 &copy; 2021</p>
    </footer>      
  </body>
</html>