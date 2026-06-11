import React, {useState} from 'react';
import {useHistory} from 'react-router-dom';
import AppContext from '../context/AppContext';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {Api, login} from '../context/Actions';

const Login = () => {
    const {dispatch} = React.useContext(AppContext);
    let history = useHistory();

    const [userID, setUserID] = useState('');
    const [password, setPassword] = useState('');
    const [isRegistering, setIsRegistering] = useState(false);
    const [name, setName] = useState('');
    const [birthdate, setBirthdate] = useState('');
    const [birthplace, setBirthplace] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        const url = '/persons/' + userID;
        const array = userID.toLowerCase().split("@");
        const username = array[0];
        try {
            const res = await Api.get(url);
            if (res.status === 200) {
                dispatch(login(userID, username));
                history.push("/mypage");
            }
        } catch (err) {
            alert('Usuario no encontrado. Por favor regístrate.');
        }
    }

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const res = await Api.post('/persons', {
                email: userID,
                name: name,
                birthdate: birthdate,
                birthplace: birthplace
            });
            if (res.status === 201) {
                alert('Usuario registrado exitosamente. Ahora puedes iniciar sesión.');
                setIsRegistering(false);
            }
        } catch (err) {
            alert('Error al registrar: ' + err.response.data.message);
        }
    }

    return (
        <div className="outerDiv">
            <div className="innerDiv">
                <Form name="form">
                    <h3 className="loginTitle">Web App with React</h3>
                    <h4 className="secTitle">Personal Finance Management</h4>

                    <Form.Group controlId={userID}>
                        <Form.Label>Email </Form.Label>
                        <Form.Control type="text" placeholder="Enter Email" autoFocus value={userID} required onChange={(e) => setUserID(e.target.value)}/>
                    </Form.Group>

                    <Form.Group controlId="password">
                        <Form.Label>Password </Form.Label>
                        <Form.Control type="password" placeholder="Enter Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                    </Form.Group>

                    {isRegistering && (
                        <>
                            <Form.Group>
                                <Form.Label>Nombre Completo</Form.Label>
                                <Form.Control type="text" placeholder="Enter Name" value={name} onChange={(e) => setName(e.target.value)}/>
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Fecha de Nacimiento</Form.Label>
                                <Form.Control type="date" value={birthdate} onChange={(e) => setBirthdate(e.target.value)}/>
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Lugar de Nacimiento</Form.Label>
                                <Form.Control type="text" placeholder="Enter Birthplace" value={birthplace} onChange={(e) => setBirthplace(e.target.value)}/>
                            </Form.Group>
                        </>
                    )}

                    <br/>
                    {!isRegistering ? (
                        <>
                            <Button variant="outline-primary" size="sm" block type="submit" onClick={handleLogin}>Login</Button>
                            <br/>
                            <Button variant="outline-success" size="sm" block onClick={() => setIsRegistering(true)}>Registrarse</Button>
                        </>
                    ) : (
                        <>
                            <Button variant="outline-success" size="sm" block type="submit" onClick={handleRegister}>Crear Cuenta</Button>
                            <br/>
                            <Button variant="outline-secondary" size="sm" block onClick={() => setIsRegistering(false)}>Volver al Login</Button>
                        </>
                    )}
                </Form>
            </div>
        </div>
    );
};

export default Login;
