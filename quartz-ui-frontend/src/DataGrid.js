import React, { Component } from 'react';
import { Table, Pagination } from 'react-bootstrap'

class DataGrid extends Component {
  state = {
    activePage: 1
  }

  constructor(props) {
    super(props);

    // this.state = {
    //   activePage: props.initPage
    // };
  }

  renderRow = (columns, row) => {
    return (
      <tr>
        {columns.map((column, i) => {
          return (
            <td>{row[column.name]}</td>
          )
        })}
      </tr>
    )
  }

  renderTable = (columns, rowData) => {
    return (
      <Table bordered hover size="sm">
        <thead>
          <tr>
            {columns.map((column, i) => {
              return (
                <th>{column.name}</th>
              )
            })}
          </tr>
        </thead>
        <tbody>
          {rowData.map((row, i) => {
            return this.renderRow(columns, row)
          })}
        </tbody>
      </Table>
    );
  }

  changeActivePage = (page) => {
    this.setState({activePage: page});
    this.props.onPageChange(page);
  }

  renderPage = () => {
    let items = [];
    let startPage = Math.floor((this.state.activePage - 1)/10)*10+1;
    let endPage = Math.ceil(this.state.activePage/10)*10;
    
    for (let number = startPage; number <= endPage && number <= this.props.totalPages; number++) {
      items.push(
        <Pagination.Item 
          key={number} 
          active={number === this.state.activePage}
          onClick={() => {this.changeActivePage(number)}}>
          {number}
        </Pagination.Item>
      );
    }
    
    return (
      <Pagination>
        {this.state.activePage > 10 && <Pagination.Prev/>}
        {items}
        {this.props.totalPages > endPage && <Pagination.Next/>}
      </Pagination> 
    )
  }

  render() {
    // const maxPage = Math.ceil(this.state.activePage / 10) * 10;
    // const nextItem = this.props.totalPages > maxPage ? '>' : null;
    // console.log(maxPage)
    // console.log(nextItem)
    const totalPage = this.state.activePage <= 5 ? 5 : this.state.activePage + 2;
    return (
      <div>
        {this.renderTable(this.props.columns, this.props.rowData)}
        {this.renderPage()}
      </div>
    );
  }

}

export default DataGrid;