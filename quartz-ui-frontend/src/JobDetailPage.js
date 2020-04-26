import React, { Component }  from 'react';
import { withRouter } from 'react-router-dom';
import { Container, Row } from 'react-bootstrap';
import DataGrid from './DataGrid';
import * as schedulerService from './SchedulerAPI';

class JobDetailPage extends Component {
  state = {
    data: [],
    totalPages: 0
  }

  componentDidMount() {
    schedulerService.getJobHistory(this.props.match.params.job)
    .then(res => {
      this.setState({
        data: res.data.content,
        totalPages: res.data.totalPages
      });
    }); 
  }

  handlePageChange = (page) => {
    schedulerService.getJobHistory(this.props.match.params.job, page)
    .then(res => {
      this.setState({
        data: res.data.content,
        totalPages: res.data.totalPages
      });
    }); 
  }

  render() {
    const columns = [
      {'name': 'schedName'},
      {'name': 'entryId'},
      {'name': 'jobName'},
      {'name': 'jobGroup'},
    ];
    return (
      <div>
        <Container>
          {this.props.match.params.job}
          <DataGrid 
            columns={columns}
            rowData={this.state.data}
            totalPages={this.state.totalPages}
            onPageChange={this.handlePageChange}
          />
        </Container>
      </div>
    )
  }
}

export default withRouter(JobDetailPage);