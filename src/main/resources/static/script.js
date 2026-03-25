let tasks = []
let editingTaskId = null

function openCreateModal(){
document.getElementById("createModal").style.display="flex"
}

function closeCreateModal(){
document.getElementById("createModal").style.display="none"
}

window.onclick=function(event){
const modal=document.getElementById("createModal")
if(event.target===modal){
closeCreateModal()
}
}

async function loadTasks(){

const response=await fetch("http://localhost:8080/tasks")
tasks=await response.json()

renderTasks()
updateStatusOverview()

}

function updateStatusOverview(){

const todo=tasks.filter(t=>t.status==="TODO").length
const progress=tasks.filter(t=>t.status==="IN_PROGRESS").length
const done=tasks.filter(t=>t.status==="DONE").length
const cancelled=tasks.filter(t=>t.status==="CANCELLED").length

document.getElementById("todoCount").innerText=todo
document.getElementById("progressCount").innerText=progress
document.getElementById("doneCount").innerText=done
document.getElementById("cancelledCount").innerText=cancelled

}

async function createTask(){

const title=document.getElementById("title").value.trim()
const description=document.getElementById("description").value
const priority=document.getElementById("priority").value
const error=document.getElementById("titleError")

error.innerText=""

if(!title){
error.innerText="Title cannot be empty"
return
}

const task={
title,
description,
priority,
status:"TODO"
}

await fetch("http://localhost:8080/tasks",{
method:"POST",
headers:{ "Content-Type":"application/json" },
body:JSON.stringify(task)
})

document.getElementById("title").value=""
document.getElementById("description").value=""

closeCreateModal()

loadTasks()

}

function renderTasks(){

const list=document.getElementById("taskList")
const filter=document.getElementById("taskFilter")?.value || "ALL"

let filteredTasks=tasks

if(filter==="ACTIVE"){
filteredTasks=tasks.filter(t=>t.status==="TODO" || t.status==="IN_PROGRESS")
}
else if(filter==="DONE"){
filteredTasks=tasks.filter(t=>t.status==="DONE")
}
else if(filter==="CANCELLED"){
filteredTasks=tasks.filter(t=>t.status==="CANCELLED")
}

let html=""

filteredTasks.forEach(task=>{
html+=renderTask(task)
})

if(html===""){
html=`<div class="empty-state">No tasks found</div>`
}

list.innerHTML=html

}

function renderTask(task){

if(editingTaskId===task.id){
return renderEditMode(task)
}

return renderViewMode(task)

}

function renderViewMode(task){

let workflowButton=""

if(task.status==="TODO"){
workflowButton=`<button onclick="updateStatus('${task.id}','IN_PROGRESS')">Start</button>`
}
else if(task.status==="IN_PROGRESS"){
workflowButton=`<button onclick="updateStatus('${task.id}','DONE')">Mark Done</button>`
}
else if(task.status==="DONE"){
workflowButton=`<button onclick="updateStatus('${task.id}','IN_PROGRESS')">Undo Done</button>`
}

let cancelButton=""
if(task.status==="TODO" || task.status==="IN_PROGRESS"){
cancelButton=`<button class="cancel-btn" onclick="cancelTask('${task.id}')">Cancel</button>`
}

let editButton=""
if(task.status!=="CANCELLED"){
editButton=`<button onclick="editTask('${task.id}')">Edit</button>`
}

if(task.status==="CANCELLED"){
workflowButton=`<span class="cancelled">❌ Cancelled</span>`
}

return `

<div class="task-card">

<div class="task-title">${task.title}</div>

<div class="task-desc">${task.description || ""}</div>

<div class="task-meta">
Priority: ${task.priority}
<br>
Status: ${task.status}
</div>

<div class="task-actions">
${workflowButton}
${editButton}
${cancelButton}
</div>

</div>

`

}

function renderEditMode(task){

return `

<div class="task-card">

<input id="edit-title-${task.id}" value="${task.title}">

<textarea id="edit-desc-${task.id}">${task.description || ""}</textarea>

<select id="edit-priority-${task.id}">
<option value="LOW" ${task.priority==="LOW"?"selected":""}>Low</option>
<option value="MEDIUM" ${task.priority==="MEDIUM"?"selected":""}>Medium</option>
<option value="HIGH" ${task.priority==="HIGH"?"selected":""}>High</option>
</select>

<div class="task-actions">
<button onclick="saveTask('${task.id}')">Save</button>
<button onclick="cancelEdit()">Cancel</button>
</div>

</div>

`
}

function editTask(id){
editingTaskId=id
renderTasks()
}

function cancelEdit(){
editingTaskId=null
renderTasks()
}

async function updateStatus(id,status){

const task=tasks.find(t=>String(t.id)===String(id))

const updatedTask={...task,status:status}

await fetch(`http://localhost:8080/tasks/${id}`,{
method:"PUT",
headers:{ "Content-Type":"application/json" },
body:JSON.stringify(updatedTask)
})

loadTasks()

}

async function cancelTask(id){

const confirmCancel=confirm("Cancel this task permanently?")
if(!confirmCancel) return

const task=tasks.find(t=>String(t.id)===String(id))

const updatedTask={...task,status:"CANCELLED"}

await fetch(`http://localhost:8080/tasks/${id}`,{
method:"PUT",
headers:{ "Content-Type":"application/json" },
body:JSON.stringify(updatedTask)
})

loadTasks()

}

document.addEventListener("DOMContentLoaded",()=>{
loadTasks()
})