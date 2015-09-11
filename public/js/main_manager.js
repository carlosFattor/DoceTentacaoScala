  $(document).ready(function(){
  $('nav').hover(function(e){
    e.preventDefault();
    e.stopPropagation();
      $('nav').addClass('nav-hover');
      $('.main-content').addClass('push');
      $('nav ul .menu-item').stop().fadeIn(1200);
  },function(){
      $('.subnav').fadeOut('fast');
      $('.menu-item').hide();
      $('nav').removeClass('nav-hover');
      $('.main-content').removeClass('push');

  });

  // Display Sub Menu

    $('nav ul .has-sub').click(function(e){
      e.preventDefault();
      e.stopPropagation();
      $('.subnav').filter(':not(:animated)').slideToggle();
    });

  // Header User Panel

   $('header .userpanel .name').click( function(e){
            e.preventDefault();
            e.stopPropagation();
          $('header .userpanel .name').toggleClass('active');
          $('header .userpanel .name span').toggleClass('arrow');
          $("header .userpanel .drop").stop().fadeToggle(500);
      });
  $('.main-content').click( function(){
          $('header .userpanel .name').removeClass('active');
          $('header .userpanel .name span').removeClass('arrow');
          $("header .userpanel .drop").filter(':not(:animated)').fadeOut();
      });

  // Container Scroll Bar

     $("html").niceScroll({styler:"fb",cursorcolor:"#e8403f", cursorwidth: '6', cursorborderradius: '10px', background: '#404040', spacebarenabled:false,  cursorborder: '', zindex: '1000'});
  });

  //Email Validator
function checkForm(form){

  if(form.password.value.length  < 6){
    form.password.focus();
    form.password.innerHTML = "Senha menor que 6 digitos";
    return false;
  }

  if(checkEmail(form.email)){
    if(!checkEmail(form.repEmail)){
      form.repEmail.focus();
      form.repEmail.innerHTML = "Repita o E-mail correto";
      return false;
    }
  }else{
    form.email.focus();
    form.email.innerHTML = "E-mail incorreto";
    return false;
  }

}

function checkEmail(email) {
        var re = /^([\w_\-\.])+@([\w_\-\.])+\.([\w]{2,4})$/;
        return !re.test(email) ? false : true;
    }
