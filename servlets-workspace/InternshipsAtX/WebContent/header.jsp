<!-- navigation bar -->
<nav class="navbar navbar-dark bg-dark">
  <div class="container-fluid justify-content-start">
  ${ (user == null) ? '<a class="navbar-brand" href="/home"><img src="images/logo.png" style="max-height: 35px;">Internship Management</a>' : '<a class="navbar-brand" href="/dashboard"><img src="images/logo.png" style="max-height: 35px;">Internship Management</a>'}
  <div class="ml-auto d-flex">
    <div class="nav-item dropdown">
      <a class="text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">${user.name} (${user.role})</a>
      <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="right:0;left:auto;">
        ${ (user.role == "Admin") ? '<li><a class="dropdown-item" href="./user-management">User management</a></li>' : '' }
        ${ (user.role == "Admin" || user.role == "Professor" || user.role == "Assistant") ? '<li><a class="dropdown-item" href="./student-management">Student management</a></li>' : '' }
        ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./program-management">Program management</a></li>' : '' }
        ${ (user.role == "Admin" || user.role == "Professor" || user.role == "Assistant") ? '<li><a class="dropdown-item" href="./topic-management">Topic management</a></li>' : '' }
        ${ (user.role == "Admin" || user.role == "Professor" || user.role == "Assistant") ? '<li><a class="dropdown-item" href="./defense-management">Defense management</a></li>' : '' }
        ${ (user.role == "Student") ? '<li><a class="dropdown-item" href="./student-view">My internship</a></li>' : '' }
        <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
      </ul>
  	</div>
  </div>
</nav>

<script type="text/javascript">
$(document).ready(function() {
    $(".dropdown-toggle").dropdown();
});
</script>
