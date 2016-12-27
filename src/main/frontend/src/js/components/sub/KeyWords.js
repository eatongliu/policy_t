var React = require('react');
import { Tag, Input, Tooltip, Button } from 'antd';

class KeyWords extends React.Component {
  state = {
    inputVisible: false,
    inputValue: '',
  };
// 删除标签=======================dispatch
  handleClose = (removedTag) => {
    const keyWords = this.props.keyWords.filter(tag => tag !== removedTag);
    console.log(keyWords);
    this.props.dispatch({ keyWords:keyWords });
  }
//显示输入框
  showInput = () => {this.setState({ inputVisible: true }, () => this.input.focus());}
// 更改新增
  handleInputChange = (e) => {this.setState({ inputValue: e.target.value });}
//　确认新增======================dispatch
  handleInputConfirm = () => {
    const state = this.state;
    const inputValue = state.inputValue;
    let keyWords = this.props.keyWords;
    if (inputValue && keyWords.indexOf(inputValue) === -1) {
      keyWords = [...keyWords, inputValue];
    }
		var self=this;
    console.log(keyWords);
    this.setState({
      inputVisible: false,
      inputValue: '',
    },()=>{
			self.props.dispatch({keyWords:keyWords})
		});
  }
  saveInputRef = input => this.input = input
  render() {
    const { inputVisible, inputValue } = this.state;
		const keyWords=this.props.keyWords;
    return (
      <div style={{padding:'0 0 0 44px'}}>
        {keyWords.map((tag, index) => {
          const isLongTag = tag.length > 20;
          const tagElem = (
            <Tag key={tag} closable={true} afterClose={() => this.handleClose(tag)}>
              {isLongTag ? `${tag.slice(0, 20)}...` : tag}
            </Tag>
          );
          return isLongTag ? <Tooltip title={tag}>{tagElem}</Tooltip> : tagElem;
        })}
        {inputVisible && (
          <Input
            ref={this.saveInputRef}
            type="text" size="small"
            style={{ width: 78 }}
            value={inputValue}
            onChange={this.handleInputChange}
            onBlur={this.handleInputConfirm}
            onPressEnter={this.handleInputConfirm}
          />
        )}
        {!inputVisible && <Button size="small" type="dashed" onClick={this.showInput}>+ 添加</Button>}
      </div>
    );
  }
}
export default KeyWords
