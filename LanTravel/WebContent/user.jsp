<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>íì ì ë³´ìì </title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
    <link rel="stylesheet" href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
    <link rel="stylesheet" href="styles/user.css" />
    <link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css" rel="stylesheet" />
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
    <div class="update-form">
      <h1>íì ì ë³´ ìì íê¸°</h1>
      <form>
        <fieldset>
          <legend>ì¬ì©ì ì ë³´</legend>
          <table class="updateTable">
            <tr>
              <th>ìì´ë</th>
              <td>ìì´ë</td>
            </tr>
            <tr>
              <th>ëë¤ì</th>
              <td><input type="text" value="ëë¤ì" name="alias" required /></td>
            </tr>
            <tr>
              <th>í¨ì¤ìë</th>
              <td><input type="password" value="í¨ì¤ìë" name="pass1" required /></td>
            </tr>
            <tr>
              <th>ì´ë©ì¼</th>
              <td><input type="email" value="ì´ë©ì¼" name="email" required /></td>
            </tr>
            <tr>
              <th>ì íë²í¸</th>
              <td><input type="tel" value="ì íë²í¸" name="tel" required /></td>
            </tr>
          </table>
        </fieldset>
        <div>
          <input type="submit" value="ìì íê¸°" class="update-btn" />
          <button type="button" onclick="location.href='login.html'" class="update-btn">ëìê°ê¸°</button>
        </div>
      </form>
    </div>
    <footer>
      <p>Database(COMP322005) Team8 &copy; 2021</p>
    </footer>
  </body>
</html>
