# app.py
import streamlit as st
import requests
import pandas as pd

BACKEND_URL = "http://localhost:8080/api"  # Replace with your backend URL

def main():
    st.title("Team Formation Application")
    menu = ["Login", "Sign Up"]
    choice = st.sidebar.selectbox("Menu", menu)

    if choice == "Login":
        srn = st.sidebar.text_input("SRN")
        password = st.sidebar.text_input("Password", type="password")
        role = st.sidebar.selectbox("Role", ["student", "teacher"])
        if st.sidebar.checkbox("Login"):
            payload = {"srn": srn, "password": password, "role": role}
            print(payload)
            response = requests.post(f"{BACKEND_URL}/auth/login", json=payload)
            print(response.status_code)
            if response.status_code == 200:
                user = response.json()
                st.success(f"Logged in as {user['role']}")
                if user["role"] == "student":
                    student_dashboard(user["srn"])
                elif user["role"] == "teacher":
                    teacher_dashboard(user["srn"])
            else:
                st.error("Invalid SRN or password")

    elif choice == "Sign Up":
        st.subheader("Create New Account")
        new_srn = st.text_input("SRN")
        new_name = st.text_input("Name")
        new_password = st.text_input("Password", type="password")
        role = st.selectbox("Role", ["student", "teacher"])
        if st.button("Create Account"):
            payload = {"srn": new_srn, "name": new_name, "password": new_password, "role": role}
            response = requests.post(f"{BACKEND_URL}/teams/users", json=payload)
            if response.status_code == 200:
                st.success("Account created successfully!")
            else:
                st.error("Failed to create account")

def student_dashboard(student_srn):
    import requests
    st.subheader("Student Dashboard")

    # Send Team Mapping Request
    st.subheader("Send Team Mapping Request")
    student1_srn = student_srn
    student2_srn = st.text_input("Enter SRN of Student 2")
    student3_srn = st.text_input("Enter SRN of Student 3")
    student4_srn = st.text_input("Enter SRN of Student 4")
    teachers = requests.get(f"{BACKEND_URL}/teams/teachers").json()
    teacher_dict = {teacher['name']: teacher['srn'] for teacher in teachers}
    teacher_name = st.selectbox("Select Teacher", list(teacher_dict.keys()))
    domain = st.text_input("Domain")
    problem_statement = st.text_area("Problem Statement")
    if st.button("Send Request"):
        payload = {
          "student1Srn": student1_srn,
          "student2Srn": student2_srn,
          "student3Srn": student3_srn,
          "student4Srn": student4_srn,
          "teacherSrn": teacher_dict[teacher_name],
          "domain": domain,
          "problemStatement": problem_statement,
        }
        print(payload)
        response = requests.post(f"{BACKEND_URL}/teams/requests", json=payload)
        if response.status_code == 200:
            st.success("Team mapping request sent successfully!")
        else:
            st.error("Failed to send team mapping request")

    # View Team Mapping Requests
    st.subheader("Pending Team Mapping Requests")
    response = requests.get(f"{BACKEND_URL}/teams/student/{student_srn}")
    http_response = response.json()
    print(http_response)
    for request in http_response:
        st.caption(f"Request ID: {request['id']}")
        st.write(f"Domain: {request['domain']}")
        st.write(f"Problem Statement: {request['problemStatement']}")
        st.write(f"Request Accepted: {request['requestAccepted']}")
        df = pd.DataFrame(
            [[request["student1Name"], request["student2Name"], request["student3Name"], request["student4Name"], request["teacherName"]]],
            columns=["SRN1", "SRN2", "SRN3", "SRN4", "Mentor"],
        )
        st.dataframe(df)

def teacher_dashboard(teacher_srn):
    import requests
    st.subheader("Teacher Dashboard")

    # View Team Mapping Requests
    st.subheader("Team Mapping Requests")
    response = requests.get(f"{BACKEND_URL}/teams/teacher/{teacher_srn}")
    http_response = response.json()
    for request in http_response:
        if not request["requestAccepted"]:
            st.write(f"Request ID: {request['id']}")
            df = pd.DataFrame(
                [[request["student1Srn"], request["student2Srn"], request["student3Srn"], request["student4Srn"]]],
                columns=["SRN1", "SRN2", "SRN3", "SRN4"],
            )
            st.dataframe(df)
            st.write(f"Domain: {request['domain']}")
            st.write(f"Problem Statement: {request['problemStatement']}")
            st.write(f"Request Accepted: {request['requestAccepted']}")
            if st.button(f"Approve Request {request['id']}"):
                payload = {"accepted": True}
                response = requests.put(f"{BACKEND_URL}/teams/requests/{request['id']}", json=payload)
                if response.status_code == 200:
                    st.success(f"Request {request['id']} approved!")
                else:
                    st.error(f"Failed to approve request {request['id']}")
            if st.button(f"Reject Request {request['id']}"):
                payload = {"accepted": False}
                response = requests.put(f"{BACKEND_URL}/teams/requests/{request['id']}", json=payload)
                if response.status_code == 200:
                    st.warning(f"Request {request['id']} rejected!")
                else:
                    st.error(f"Failed to reject request {request['id']}")

if __name__ == "__main__":
    main()