//加载模块css
require('./css/header.css');
 import { Navbar,Nav,NavItem,Modal } from 'react-bootstrap';
var React = require('react');

class Header extends React.Component{
    //弹出层按钮
    //
    constructor(props) {
    super(props);
    this.state = {
      showModal: false
        };
      }

  close() {
    this.setState({ showModal: false });
  }

  open() {
    this.setState({ showModal: true });
  }
    render(){
        return <div className='container-fluid head'>
        <div className='container'>
    <Navbar inverse>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="./index.html" className='head-bg'>政策通欢迎你！</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav pullRight>
            <NavItem onClick={this.open.bind(this)} href="#">登录</NavItem>
            <NavItem href="#">注册</NavItem>
            <NavItem href="./sub.html">订阅</NavItem>
            <NavItem href="./help.html">遇到问题</NavItem>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
       <div>

        <Modal show={this.state.showModal} onHide={this.close.bind(this)}>
          <Modal.Header closeButton>
                <div className='col-lg-6'><a>登录</a></div>
                <div className='col-lg-5'><a>注册</a></div>
            <Modal>
            </Modal>
          </Modal.Header>
          <Modal.Body>
            <div className='col-lg-offset-2 col-lg-8'>
                <input type="text" className="form-control" placeholder="账号"/>
            </div>
            <div className='col-lg-offset-2 col-lg-8'>
                <input type="text" className="form-control" placeholder="密码"/></div>
            <div className='col-lg-offset-2 col-lg-8'>
                <div className='col-lg-6'>记住我</div>
                <div style={{textAlign:'right'}} className='col-lg-6'>忘记密码？</div>
            </div>
            <div className='col-lg-offset-2 col-lg-8'><button className="form-control">登录</button></div>

         </Modal.Body>
          <Modal.Footer>
            <div className='col-lg-12'>第三方登录未上线</div>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
    </div>
    }
}
module.exports= Header
