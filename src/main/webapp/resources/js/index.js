function viewUserDetail(userId) {
    const url = `http://localhost:8081/admin/users/${userId}`;
    window.location.href = url;
}

function getAllUserByFullName(input) {
    const inputValue = input.value;
    axios.get(`http://localhost:8081/api/v1/users?name=${inputValue}`)
        .then(response => {
            const users = response.data;
            updateTable(users);
        })
        .catch(e => {
            console.log(e.response)
        })

}

function confirmDeleteUser(userId) {
    const confirmation = window.confirm("Are you sure you want to delete this user?");
    if (confirmation) {
        deleteUser(userId);
    }
}

function deleteUser(userId) {
    axios.delete(`http://localhost:8081/api/v1/users/${userId}`)
        .then(response => {
            window.location.href = "http://localhost:8081/admin/users";
        })
        .catch(e => {
            console.log(e.response)
        })
}

function getGroupByName(input) {
    const inputValue = input.value;
    let groups;
    let countUserOfGroup = new Map();
    axios.get(`http://localhost:8081/api/v1/groups?name=${inputValue}`)
        .then(response => {
            groups = response.data;
            return Promise.all(groups.map(g => {
                return axios.get(`http://localhost:8081/api/v1/groups/${g.id}/users/count`)
                    .then(response => response.data)
                    .catch(e => {
                        return 0;
                    });
            }));
        })
        .then(counts => {
            groups.forEach((g, index) => {
                countUserOfGroup.set(g.id, counts[index]);
            });
            updateGroupTable(groups, countUserOfGroup);
        })
        .catch(e => {
            console.log(e.response);
        });
}

function editGroup(groupId) {
    const url = `http://localhost:8081/admin/groups/${groupId}`;
    window.location.href = url;
}

function confirmDeleteGroup(groupId) {
    const confirmation = window.confirm("Are you sure you want to delete this group?");
    if (confirmation) {
        deleteGroup(groupId);
    }
}

function deleteGroup(groupId) {
    axios.delete(`http://localhost:8081/api/v1/groups/${groupId}`)
        .then(response => {
            window.location.href = "http://localhost:8081/admin/groups";
        })
        .catch(e => {
            console.log(e.response.data)
        })
}

function confirmDeletePost(postId) {
    const confirmation = window.confirm("Are you sure you want to delete this post?");
    if (confirmation) {
        deletePost(postId);
    }
}

function deletePost(postId) {
    axios.delete(`http://localhost:8081/api/v1/posts/${postId}`)
        .then(response => {
            window.location.href = "http://localhost:8081/admin/posts";
        })
        .catch(e => {
            console.log(e.response.data)
        })
}

function addNewGroup() {
    const url = `http://localhost:8081/admin/groups/add`;
    window.location.href = url;
}

function viewUsersGroup(groupId) {
    const url = `http://localhost:8081/admin/groups/${groupId}/members`;
    window.location.href = url;
}

function confirmDeleteUserFromGroup(groupId, userId) {
    const confirmation = window.confirm("Are you sure you want to delete this group?");
    if (confirmation) {
        deleteUserFromGroup(groupId, userId);
    }
}

function deleteUserFromGroup(groupId, userId) {
    axios.delete(`http://localhost:8081/api/v1/groups/${groupId}/users/${userId}/delete`)
        .then(response => {
            window.location.href = "http://localhost:8081/admin/groups";
        })
        .catch(e => {
            console.log(e.response.data)
        })
}

function confirmAlumni(userId) {
    axios.post(`http://localhost:8081/api/v1/users/${userId}/confirm`)
        .then(response => {
            console.log(response.data);
            axios.get(`http://localhost:8081/api/v1/alumni`)
                .then(response => {
                    const users = response.data;
                    console.log(users)
                    updateTable(users);
                })
                .catch(error => {
                    console.error('Error fetching filtered users:', error);
                });
        })
        .catch(error => {
            console.log(error)
        });


}

function filterUsers() {
    const selectedRole = document.getElementById("userRoleFilter").value;
    console.log(selectedRole)
    switch (selectedRole) {
        case 'alumni':
            axios.get(`http://localhost:8081/api/v1/alumni`)
                .then(response => {
                    const users = response.data;
                    console.log(users)
                    updateTable(users);
                })
                .catch(error => {
                    console.error('Error fetching filtered users:', error);
                });
            break;
        case 'lecturer':
            axios.get(`http://localhost:8081/api/v1/lecturers`)
                .then(response => {
                    const users = response.data;
                    updateTable(users);
                })
                .catch(error => {
                    console.error('Error fetching filtered users:', error);
                });
            break;
        default:
            axios.get(`http://localhost:8081/api/v1/users`)
                .then(response => {
                    const users = response.data;
                    updateTable(users);
                })
                .catch(error => {
                    console.error('Error fetching filtered users:', error);
                });
            break;
    }
}

function hasIsConfirm(usersArray) {
    return usersArray.some(user => user.hasOwnProperty("isConfirmed"));
}

function hasUserId(usersArray) {
    return usersArray.some(user => user.hasOwnProperty("userId"));
}

function updateTable(users) {
    const tbody = document.querySelector(".table tbody");
    tbody.innerHTML = "";
    const hasIsConfirmProperty = hasIsConfirm(users);
    if (hasIsConfirmProperty && hasUserId(users)) {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                    <button class="btn ${user.isConfirmed ? ' btn-success' : 'btn-primary'}" 
                            ${user.isConfirmed ? 'disabled' : ''}
                            onclick="confirmAlumni(${user.userId})"
                    >
                        Confirm
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    } else if (hasUserId(users)) {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    } else {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    }
}

function updateGroupTable(groups, numOfUsers) {
    const tbody = document.querySelector(".table tbody");
    tbody.innerHTML = "";
    console.log(groups)
    console.log(numOfUsers)
    groups.forEach(g => {
        const newRow = `<tr>
                    <td>${g.id}</td>
                    <td>${g.groupName}</td>
                    <td>${g.creatorId}</td>
                    <td>${numOfUsers.get(g.id)}</td>
                    <td class="action-buttons">
                        <button class="btn btn-warning" onclick="viewUsersGroup(${g.id})">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-warning" onclick="editGroup(${g.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-danger" onclick="confirmDeleteGroup(${g.id})">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                        <button class="btn btn-primary" onclick="sendToAllMemberInGroup(${g.id})">
                            <i class="fas fa-bell"></i>
                        </button>
                    </td>
                </tr>`;
        tbody.innerHTML += newRow;
    });
}

//=================SURVEY======================
function addNewSurvey() {
    const url = `http://localhost:8081/admin/surveys/add`;
    window.location.href = url;
}

function filterRelateSurvey() {
    const selected = document.getElementById("surveyFilter").value;
    switch (selected) {
        case 'question':
            // axios.get(`http://localhost:8081/api/v1/alumni`)
            //     .then(response => {
            //         const users = response.data;
            //         console.log(users)
            //         updateTable(users);
            //     })
            //     .catch(error => {
            //         console.error('Error fetching filtered users:', error);
            //     });
            break;
        case 'response':
            // axios.get(`http://localhost:8081/api/v1/lecturers`)
            //     .then(response => {
            //         const users = response.data;
            //         updateTable(users);
            //     })
            //     .catch(error => {
            //         console.error('Error fetching filtered users:', error);
            //     });
            break;
        default:
            // axios.get(`http://localhost:8081/api/v1/users`)
            //     .then(response => {
            //         const users = response.data;
            //         updateTable(users);
            //     })
            //     .catch(error => {
            //         console.error('Error fetching filtered users:', error);
            //     });
            break;
    }
}

function editSurvey(surveyId) {
    const url = `http://localhost:8081/admin/surveys/${surveyId}`;
    window.location.href = url;
}

function confirmDeleteSurvey(surveyId) {
    const confirmation = window.confirm("Are you sure you want to delete this survey?");
    if (confirmation) {
        deleteSurvey(surveyId);
    }
}

function deleteSurvey(surveyId) {
    axios.delete(`http://localhost:8081/api/v1/surveys/${surveyId}`)
        .then(response => {
            window.location.href = "http://localhost:8081/admin/surveys";
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateTableSurveyOrQuestionOrResponse(type, arrays) {
    const tbody = document.querySelector(".table tbody");
    tbody.innerHTML = "";
    const hasIsConfirmProperty = hasIsConfirm(users);
    if (hasIsConfirmProperty && hasUserId(users)) {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                    <button class="btn ${user.isConfirmed ? ' btn-success' : 'btn-primary'}" 
                            ${user.isConfirmed ? 'disabled' : ''}
                            onclick="confirmAlumni(${user.userId})"
                    >
                        Confirm
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    } else if (hasUserId(users)) {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.userId}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    } else {
        users.forEach(user => {
            const newRow = `<tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.fullName}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td class="user-avatar">
                    <img src="${user.avatarLink}"/>
                </td>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUserDetail(${user.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteUser(${user.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>`;
            tbody.innerHTML += newRow;
        });
    }
}


//=============SEND notification===========
//send to all alumni
function sendToAllMemberInGroup(groupId) {
    const url = `http://localhost:8081/admin/groups/${groupId}/send-notification`;
    window.location.href = url;
}

document.getElementById("sendMailForm").addEventListener("submit", function (event) {
    event.preventDefault();
    const params = {
        subject: document.getElementById('subject').value,
        body: document.getElementById('body').value,
        from: document.getElementById('from').value
    };
    const groupId = document.getElementById('groupId').value;
    if (typeof groupId !== "undefined" && groupId !== null) {
        axios.post(`http://localhost:8081/api/v1/groups/${groupId}/mails`, params)
            .then(response => {
                window.location.href = "http://localhost:8081/admin";
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } else {
        axios.post('http://localhost:8081/api/v1/groups/mails', JSON.stringify(params), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                window.location.href = "http://localhost:8081/admin";
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

})
