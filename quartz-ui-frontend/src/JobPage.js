import React, { Component } from 'react';
import { Container, Row } from 'react-bootstrap';
import DataGrid from './DataGrid';
import * as schedulerService from './SchedulerAPI';

class JobPage extends Component {

  state = {
    jobData: []
  }

  componentDidMount() {
    schedulerService.getJobs().then(res => {
      console.log(res.data.content)
      this.setState({jobData: res.data.content});
    }); 
  }

  render() {
    const jobColumns = [
      {'name': 'jobName'},
      {'name': 'jobClassName'},
      {'name': 'triggerType'},
      {'name': 'triggerState'},
    ];
    return (
      <Container style={{ marginTop: '7em' }}>
        {/* <Header as='h1'>Jobs</Header> */}
        <DataGrid 
          columns={jobColumns}
          rowData={this.state.jobData}
        />
      </Container>
    )
  }
}

export default JobPage;