/**
 * 
 */
var base_location=location.href.split("?")[0];
var last_search="";
var select={};
var getSelect=()=>{
	select=$("#search_type_selector").val();
	return select;
};
var data={};
var loading=false;
// function for avoid collision
function getValues(){
	 last_search=$("#search_box").val()
	 select=$("#search_type_selector").val()
      }

$("#search_box").on('keyup', function (event) {
	if (event.keyCode === 13) {
		$("#search_btn").click(); 
	}		
  });
$("#search_btn").click(()=>{
	//if(checkPreviews()){
		getValues()
		search();
	//}
})

function search(){
	$.ajax({
		url:(base_location.replace("search/","search")+"_api"+"?type="+select+"&search="+this.last_search),
		type:"GET",
		dataType: "json",
		beforeSend:function(){
			if(!loading){
				loading=true;
				$("#table-message").css({display:"flex"});
				$("#table-message").empty();
				$("#table-message").addClass("table-msg-bg");
				let element=$("<img>",{
					src: '../assets/loading-img.gif',
					  alt: 'An image',
					  class: 'table-message-image-loading'
				});
				$("#table-message").append(element);
			}
		},
		success:function(result){
	/*		console.log(JSON.parse(result).conversation);
			let list = document.getElementById("conversation-messages");
			list.innerText="";
			$("#conversations").css(loader_off);
			(JSON.parse(result).conversation).forEach((msg)=>{
				addMessage(msg);
			})  */
			data=result;
			console.log(result);
		},
		error:function(){
			console.log("error in updateConversation")
		},
		complete:function(){
			console.log("completed")
			loading=false;
			$("#table-message").removeClass();
			$("#table-message").empty();
			$("#table-message").css({display:"none"});
			if(JSON.stringify(data)=="{}"){
				$("#table-message").css({display:"flex"});
				let element=$("<img>",{
					src: '../assets/no-record-found.png',
					  alt: 'not found',
					  class: 'table-message-image-notfound'
				});
				$("#table-message").append(element);
				setHeader();
			}
			else{
				setHeader();
				
				let products=Object.values(data);
				let url = base_location.replace("search/","");
				for(let i=0;i<products.length; i++){
					let{_id,ph,holder_name,city,last_update,post,holder_id}=products[i];
					let up = toDate(last_update)
					let po = toDate(post)
					$("#table>tbody").append(
					`<tr onclick=window.open("${url}product?_id=${_id}&holder_id=${holder_id}","_blank")>
						<td>${_id}</td>
						<td>${ph}</td>
						<td>${holder_name}</td>
						<td>${city}</td>
						<td>${up}</td>
						<td>${po}</td>
						<td><img src="https://www.freeiconspng.com/uploads/right-arrow-blue-png-2.png" style="width:15px;" atl="click here"></td>
					</tr>`
					)
				}
				$("#table").append()
			}
		}
	})
}
function setHeader(){
	$("#table").empty();
		let th=$("<tr>",{id:"header"}).append(
		`
			<th>product id</th>
			<th>ph</th>
			<th>holder</th>
			<th>city</th>
			<th>last update</th>
			<th>posted</th>
			<th></th>
			`
		);
		$("#table").append($("<tbody>").append(th));
}
function toDate(millis) {
	 // example timestamp in milliseconds
	const date = new Date(parseInt(millis)); // create a Date object from the timestamp
	const utcString = date.toUTCString(); // convert the Date object to a UTC string
	const formattedDate = utcString.slice(5, 16); // extract the date portion of the UTC string in DD/MM/YYYY format
	const formattedTime = utcString.slice(17, 22); // extract the time portion of the UTC string in hh:mm format
	const formattedDateTime = formattedDate + ' ' + formattedTime; // concatenate the date and time strings with a space in between
	return formattedDateTime;
}

