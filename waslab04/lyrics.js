
function showSongs () {
	var content = document.getElementById("content").value;
	
	// Replace the two lines below with your implementation
	req = new XMLHttpRequest();
	req.open('GET', "http://localhost:8080/waslab04/lyric_search.php?content="
		+content,/*async*/true);
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var response = JSON.parse(req.responseText);
			//console.log(response);
			//console.log(response.SearchLyricResult[0].Artist);
			var left = '';
			var i = 0;
			while (response.SearchLyricResult[i] != null) {
				var artist = response.SearchLyricResult[i].Artist;
				var song = response.SearchLyricResult[i].Song;
				var lyricId = response.SearchLyricResult[i].LyricId;
				var lyricChecksum = response.SearchLyricResult[i].LyricChecksum;
				//alert(lyricCheckSum);
				left += "<p>";
				left +="<a href='#' onclick='getLyric(" + lyricId + 
				")'>" + song + "</a>";
				left += "<input type='hidden' id='"+ lyricId + 
				"' value='"+ lyricChecksum +"'>";
				left += "(" + artist + ")</p>";
				i++;
			}
			/*
			for (i = 0; i < response.SearchLyricResult.length - 1; i++) {
				var artist = response.SearchLyricResult[i].Artist;
				var song = response.SearchLyricResult[i].Song;
				left += "<p><a href='#' onclick='getLyric( "+ song +
				")'>" + song + "</a>" + artist + "</p>";
				console.log(artist + ' ' + song);
			}*/
			document.getElementById("left").innerHTML += left;
		}
	};
	req.send(/*no params*/null);
};


function getLyric (song) {
	var lyricId = song;
	var lyricChecksum = document.getElementById(lyricId).value;
	//console.log(lyricId + ' ' + lyricChecksum);
	var link = "http://localhost:8080/waslab04/get_lyric.php?LyricId="+ 
	lyricId + "&LyricCheckSum=" + lyricChecksum;
	console.log(link);
		
	req = new XMLHttpRequest();
	req.open('GET', link,/*async*/true);
	req.onreadystatechange = function() {
		if (req.readyState == 4 && req.status == 200) {
			var response = JSON.parse(req.responseText);
			console.log(response);
			var right = '';
			right += "<div class='col'>";
			
			right += "<div class='titols'>";
			right += "<h2>"+ response.LyricSong + "</h2>";
			right += "<h3>"+ response.LyricArtist + "</h3>";
			right += "</div>";
			
			right += "<img src='"+ response.LyricCovertArtUrl + "' height='180' width='180'>";
			right += "</div>";
			
			right += "<p id='lyrics'>"+ response.Lyric + "</p>";
			document.getElementById("right").innerHTML = right;

		}
	};
	req.send(/*no params*/null);
   
}

window.onload = showSongs();
