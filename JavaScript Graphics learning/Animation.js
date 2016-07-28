
//Animation

var x = 10;
var y = 10;
var w = 20;
var h = 30;
var lr = 0;
var speed = 2;
var blue = 1;
var green = 0;
var red = 0;
var colour = "";

var bar = {	x: 10,
			y: 100,
			w: 10,
			h: 70,
			colour: "rgb(10,10,10)",
			ix: 0,
			dx: 0,
			iw: 1,
			dw: 0,
			mxw: 1, // 0=move x||1=move w
			speed: 2
		   }; // OBJECT

var canvas = document.getElementById("myAnimationCanvas");
var context = canvas.getContext("2d");
console.log(canvas.height);

var getColourOnCoordinate = function(coord, width) {
	// Just educational
	//context.clearRect(coord.x - movingBar.speed, coord.y, coord.x + coord.w + movingBar.speed, coord.y + coord.h);
	//255,0,0   -    255.255.0     -    0.255.0    -    0.255.255    -    0.0.255
	//0x . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . canvas.width
	//0x . . . . . . . 1/4 . . . . . . . . 2/4 . . . . . . 3/4 . . . . . . canvas.width
	var colour = "";
	var one_fourth = Math.ceil(width / 4);
	// In which segment are we currently at?
	var actual_one_fourth = Math.floor(coord / one_fourth);
	// Segment percent!
	var percent = ( coord - (one_fourth * actual_one_fourth)) * 100 / one_fourth;
	// Figure out the colour!
	switch (actual_one_fourth) {
		case 0:
			colour = "rgb(255, "+ Math.floor(percent * 255 / one_fourth) +", 0)";
			break;
		case 1:
			colour = "rgb(" + Math.floor(( 100 - percent) * 255 / one_fourth) +", 255, 0)";
			break;
		case 2:
			colour = "rgb(0, 255, " + Math.floor(percent * 255 / one_fourth) +")";
			break;
		case 3:
			colour = "rgb(0, " + Math.floor(( 100 - percent) * 255 / one_fourth) +", 255)";
			break;
	}
	console.log(coord.x);
	console.log(actual_one_fourth);
	console.log(percent);
	console.log(colour);
	return colour;
};

var updateMovingBar = function(movingBar) {
	
	//MOVE X
	if (!movingBar.mxw) {
		if (movingBar.ix && movingBar.x + 10 >= canvas.width - 10) {
			console.log("Decrease X");
			console.log(movingBar.x);
			console.log(movingBar.w);
			movingBar.ix = 0;
			movingBar.dx = 1;
			movingBar.iw = 0;
			movingBar.dw = 0;
		}
		else if (movingBar.dx && movingBar.x <= 10) {
			console.log("Decrease W");
			console.log(movingBar.x);
			console.log(movingBar.w);
			movingBar.mxw = 1;
			movingBar.ix = 0;
			movingBar.dx = 0;
			movingBar.iw = 0;
			movingBar.dw = 1;
		}
	}
	// MOVE W
	else if (movingBar.mxw) {
		if (movingBar.iw && movingBar.w + 10 >=  canvas.width - 10) {
			console.log("Increase X");
			console.log(movingBar.x);
			console.log(movingBar.w);
			movingBar.mxw = 0;
			movingBar.ix = 1;
			movingBar.dx = 0;
			movingBar.iw = 0;
			movingBar.dw = 0;
		}
		else if (movingBar.dw && movingBar.w - 10 <= movingBar.x) {
			console.log("Increase W");
			console.log(movingBar.x);
			console.log(movingBar.w);
			movingBar.ix = 0;
			movingBar.dx = 0;
			movingBar.iw = 1;
			movingBar.dw = 0;
		}
	}

	// Diminish the width because we are moving the starting point to the right.
	if (movingBar.ix) {
		movingBar.x += movingBar.speed;
		movingBar.w -= movingBar.speed;
		movingBar.colour = getColourOnCoordinate(movingBar.x, canvas.width);
	}
	// Increase the width because we are moving the starting point to the left.
	else if (movingBar.dx) {
		movingBar.x -= movingBar.speed;
		movingBar.w += movingBar.speed;
		movingBar.colour = getColourOnCoordinate(movingBar.x, canvas.width);
	}
	else if (movingBar.iw) {
		movingBar.w += movingBar.speed;
		movingBar.colour = getColourOnCoordinate(movingBar.w, canvas.width);
	}
	else if (movingBar.dw) {
		movingBar.w -= movingBar.speed;
		movingBar.colour = getColourOnCoordinate(movingBar.w, canvas.width);
	}
	//x = (x + speed + w <= canvas.width)? x + speed: 10;
	//return movingBar;
};

var drawMovingBar = function(coord) {
	//clearRect cleans the canvas!
	//context.clearRect(0,0,canvas.width, canvas.height);
	context.fillStyle = coord.colour;
	context.fillRect(coord.x, coord.y, coord.w, coord.h);
};

var stepMovingBar = function(movingBar) {

	updateMovingBar(movingBar);
	drawMovingBar(movingBar);
	// http://stackoverflow.com/questions/19893336/how-can-i-pass-argument-with-requestanimationframe
	//window.requestAnimationFrame(stepMovingBar.bind(movingBar));
	window.requestAnimationFrame(function() {
            stepMovingBar(movingBar);
        });
};


var updateIBar = function() {
	if (x + w >= canvas.width - 10)
		speed = -2;
	else if (x + speed <= 10)
		speed = 2;
	x = x + speed;
	//red = x < canvas.width/3;
	//green = (x > canvas.width/3) && ( x < canvas.width - canvas.width/3);
	//blue = x > canvas.width - canvas.width/3;
	colour = getColourOnCoordinate(x, canvas.width);
	//x = (x + speed + w <= canvas.width)? x + speed: 10;
};

var drawIBar = function() {
	context.fillStyle = colour;
	context.fillRect(x, y, w, h);
};

var stepIBar = function() {
	updateIBar();
	drawIBar();

	window.requestAnimationFrame(stepIBar);
};

var update= function(arg) {
	updateMovingBar(arg);
	updateIBar();
};

var draw = function(arg) {
	//clear the whole canvas!
	context.clearRect(0,0,canvas.width, canvas.height);
	drawMovingBar(arg);
	drawIBar();
};

var step = function(arg) {
	update(arg);
	draw(arg); //draw cleans as well.
	//recall the step
	window.requestAnimationFrame(function() {
		step(arg);
	});
};

step(bar);