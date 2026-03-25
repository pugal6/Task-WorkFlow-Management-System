async function fetchStatusCount(){

const status = document.getElementById("statusInput").value

const response = await fetch(`http://localhost:8080/analytics/tasks/count-by-status/${status}`)

const count = await response.json()

document.getElementById("statusResult").innerHTML =
`<p><strong>${status}</strong> tasks: ${count}</p>`

}

async function fetchCompletedTasks(){

const date = document.getElementById("dateInput").value

if(!date){
alert("Please select a date")
return
}

const response = await fetch(`http://localhost:8080/analytics/tasks/completed-on/${date}`)

const count = await response.json()

document.getElementById("completedResult").innerHTML =
`<p>Completed tasks on <strong>${date}</strong>: ${count}</p>`

}