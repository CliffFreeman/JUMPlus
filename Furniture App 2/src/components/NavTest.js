import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { FurnitureApi } from '../data/FurnitureApi';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useNavigate } from 'react-router-dom';
// import { useEffect } from 'react';

function NavScrollExample(props) {

  const navigate = useNavigate();

  const handleClick = async (e) => {
    e.preventDefault();
    const qString = document.getElementById('searchBox').value;
    const items = await FurnitureApi.getItemsByQueryString(qString);
    items.length !== 0 ? props.setProducts(items) : props.setProducts([]);
  }

  const handleLogout = (e) => {
    e.preventDefault();
    props.loggedInUser.cart = [];
    props.setLoggedInUser(null);
    navigate('/');
  }

  return (
    <>
    {props.loggedInUser !== null
    // user logged in
    ?
      <Navbar bg="dark" variant='dark' expand="lg">
       <Container fluid>
          <Navbar.Brand onClick={(e) => {e.preventDefault(); navigate('/');}} style={{fontSize: "36px"}}>Home</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/create');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>Create</Nav.Link>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/about');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>About</Nav.Link>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/contact');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>
              Contact Us
            </Nav.Link>

            <NavDropdown style={{alignSelf: "center", fontSize: "24px"}} title="Actions" id="navbarScrollingDropdown">
              <NavDropdown.Item style={{alignSelf: "center"}} onClick={handleLogout}>
                Logout
              </NavDropdown.Item>
              <NavDropdown.Item style={{alignSelf: "center"}} onClick={(e) => {e.preventDefault(); navigate('/orders');}}>Your Orders</NavDropdown.Item>
            </NavDropdown>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/cart');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>
            Your Cart ({props.loggedInUser.cart.length})
            </Nav.Link>
          </Nav>
          
          <Form className="d-flex" style={{visibility: (window.location.pathname === '/') ? "visible" : "hidden"}}>
            <Form.Control
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
              id='searchBox'
            />
            <Button id="searchButton" onClick={handleClick} style={{display: "inline"}} variant="outline-success">Search</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
    
    // no user logged in
    :
    <Navbar bg="dark" variant='dark' expand="lg">
       <Container fluid>
          <Navbar.Brand onClick={(e) => {e.preventDefault(); navigate('/');}}style={{fontSize: "36px"}} href="/">Home</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: '100px' }}
            navbarScroll
          >
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/create');}} className='link-obj' href='/create' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>Create</Nav.Link>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/about');}} className='link-obj' href='/about' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>About</Nav.Link>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/contact');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}} href="/contact">
              Contact Us
            </Nav.Link>
            <Nav.Link onClick={(e) => {e.preventDefault(); navigate('/login');}} className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>Login</Nav.Link>
            {/* <NavDropdown style={{alignSelf: "center", fontSize: "24px"}} title="Actions" id="navbarScrollingDropdown">
              <NavDropdown.Item style={{alignSelf: "center"}} onClick={handleLogout}>
                Logout
              </NavDropdown.Item>
              <NavDropdown.Item style={{alignSelf: "center"}} href="#">Your Orders</NavDropdown.Item>
            </NavDropdown>
            <Nav.Link className='link-obj' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}} href="#">
            Your Cart ({props.loggedInUser.cart.length})
            </Nav.Link>
            <Nav.Link className='link-obj' href='/login' style={{alignSelf: "center", fontSize: "24px", marginLeft: "10px"}}>Login</Nav.Link> */}
          </Nav>
          
          <Form className="d-flex" style={{visibility: (window.location.pathname === '/') ? "visible" : "hidden"}}>
            <Form.Control
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
              id='searchBox'
            />
            <Button id="searchButton" onClick={handleClick} style={{display: "inline"}} variant="outline-success">Search</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>}
  </>
  );
}

export default NavScrollExample;