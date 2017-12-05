function login() {
    var userName = parseInt(document.getElementById('userName').value);
    var password = parseInt(document.getElementById('pass').value);
    var email = parseInt(document.getElementById('email').value);
    var firstName = parseInt(document.getElementById('firstName').value);
    var lastName = parseInt(document.getElementById('lastName').value);
    var address = parseInt(document.getElementById('address').value);
    var city = parseInt(document.getElementById('city').value);
    var state = parseInt(document.getElementById('state').value);
    var zipcode = parseInt(document.getElementById('zip').value);
    var country = parseInt(document.getElementById('country').value);
    var timezone = parseInt(document.getElementById('timezone').value);
    var currency = parseInt(document.getElementById('currency').value);
}

function savings(){
    var savings = _______;
        document.getElementById('crypto').innerHTML = ;
        document.getElementById('result').innerHTML = ;
}


var cont = 0;

function register(){

     cont++;

		if(cont==1){
		 	$('.box').animate({height:'1200px'}, 550);
			$('.show').css('display','block');
      $('.box').animate({width:'400px'}, 550);
			$('#logintoregister').text('Register');
			$('#buttonlogintoregister').text('Register');
			$('#plogintoregister').text("Login");
		}
		else
		{
			$('.show').css('display','none');
			$('.box').animate({height:'365px'}, 500);
      $('.box').animate({width:'400px'}, 550);
			$('#logintoregister').text('Login');
			$('#buttonlogintoregister').text('Login');
			$('#plogintoregister').text("New to PiggyBit?");
			$('#textchange').text('Register');
			cont = 0;
		}
}