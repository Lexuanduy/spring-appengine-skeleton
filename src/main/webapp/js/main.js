firebase.initializeApp({
	apiKey : "AIzaSyAbFTa0_qIFK2gRAvGuayGwElM8DHmPwlY",
	authDomain : "yinyangpaygate.firebaseapp.com",
	databaseURL : "https://yinyangpaygate.firebaseio.com",
	projectId : "yinyangpaygate",
	storageBucket : "yinyangpaygate.appspot.com",
	messagingSenderId : "304297047277"
});
var ui = new firebaseui.auth.AuthUI(firebase.auth());
//console.log("start");
//document.cookie = 'idToken' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
//console.log("delete cookie");
var uiConfig = {
	callbacks : {
		signInSuccessWithAuthResult : function(authResult, redirectUrl) {
			// User successfully signed in.
			// Return type determines whether we continue the redirect
			// automatically
			// or whether we leave that to developer to handle.

			console.log(authResult.user.ra);
			document.cookie = 'idToken=' + authResult.user.ra;
			window.location.href = "/welcome";
			return false;
		},
		uiShown : function() {
			// The widget is rendered.
			// Hide the loader.
			document.getElementById('loader').style.display = 'none';
		}
	},
	// Will use popup for IDP Providers sign-in flow instead of the default,
	// redirect.
	signInFlow : 'popup',
	'credentialHelper' : firebaseui.auth.CredentialHelper.NONE,

	signInOptions : [

	firebase.auth.EmailAuthProvider.PROVIDER_ID

	],
	// Terms of service url.
	tosUrl : '<your-tos-url>',
	// Privacy policy url.
	privacyPolicyUrl : '<your-privacy-policy-url>'
};

ui.start('#firebaseui-auth-container', uiConfig);
