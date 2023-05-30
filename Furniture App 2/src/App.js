import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Create from './components/Create';
import About from './components/About';
import Contact from './components/Contact';
import NavScrollExample from './components/NavTest';
import { useEffect, useState } from 'react';
import Login from './components/Login';
import Cart from './components/Cart';
import Orders from './components/Orders';

function App() {

  const [products, setProducts] = useState([]);
  const [loggedInUser, setLoggedInUser] = useState(null);

  useEffect(() => {

  }, [loggedInUser])

  return (
    <div>
      <NavScrollExample setProducts={setProducts} loggedInUser={loggedInUser} setLoggedInUser={setLoggedInUser}/>
      <Routes>
        <Route path='/' element={<Home title="Welcome to Furniture Co." products={products} loggedInUser={loggedInUser} setLoggedInUser={setLoggedInUser}/>}/>
        <Route path='create' element={<Create />} />
        <Route path='contact' element={<Contact />} />
        <Route path='about' element={<About />} />
        <Route path='login' element={<Login setLoggedInUser={setLoggedInUser} />} />
        <Route path='cart' element={<Cart loggedInUser={loggedInUser} />} />
        <Route path='orders' element={<Orders loggedInUser={loggedInUser} />} />
      </Routes>
    </div>
  );
}
export default App;