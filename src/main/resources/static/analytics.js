async function fetchStatusCount(){

const status = document.getElementById("statusInput").value

// count
const response = await fetch(`http://localhost:8080/analytics/tasks/count-by-status/${status}`)
const count = await response.json()

document.getElementById("statusResult").innerHTML =
`<p><strong>${status}</strong> tasks: ${count}</p>`


// NEW: fetch tasks with that status

const tasksResponse = await fetch(`http://localhost:8080/analytics/tasks/status/${status}/tasks`)
const tasks = await tasksResponse.json()

renderStatusTasks(tasks)

}



async function fetchCompletedTasks(){

const date = document.getElementById("dateInput").value

if(!date){
alert("Please select a date")
return
}

const countResponse = await fetch(`http://localhost:8080/analytics/tasks/completed-on/${date}`)
const count = await countResponse.json()

document.getElementById("completedResult").innerHTML =
`<p>Completed tasks on <strong>${date}</strong>: ${count}</p>`


const tasksResponse = await fetch(`http://localhost:8080/analytics/tasks/completed-on/${date}/tasks`)
const tasks = await tasksResponse.json()

renderCompletedTasks(tasks)

}



function renderCompletedTasks(tasks){

const container = document.getElementById("completedTasksList")

container.innerHTML = ""

if(tasks.length === 0){
container.innerHTML = "<p>No tasks completed on this date</p>"
return
}

tasks.forEach(task => {

const div = document.createElement("div")

div.className = "task-card"

div.innerHTML = `
<p><strong>Title: </strong> ${task.title}</p>
<p><strong>Description: </strong> ${task.description || ""}</p>
<p><strong>Priority:</strong> ${task.priority}</p>
`

container.appendChild(div)

})

}



function renderStatusTasks(tasks){

const container = document.getElementById("statusResult")

if(tasks.length === 0){
container.innerHTML += "<p>No tasks with this status</p>"
return
}

tasks.forEach(task => {

const div = document.createElement("div")

div.className = "task-card"

div.innerHTML = `
<p><strong>Title: </strong> ${task.title}</p>
<p><strong>Description: </strong> ${task.description || ""}</p>
<p><strong>Priority:</strong> ${task.priority}</p>
`

container.appendChild(div)

})

}