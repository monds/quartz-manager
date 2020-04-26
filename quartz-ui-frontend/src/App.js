import React from 'react';
import { Switch, Route } from 'react-router-dom';
import JobPage from './JobPage';
import JobDetailPage from './JobDetailPage';
import 'bootstrap/dist/css/bootstrap.min.css';
// import * as schedulerService from 'SchedulerAPI';



function App() {
  return (
    <div>
      <Switch>
        <Route exact path="/" component={JobPage}/>
        <Route path="/job/:job" component={JobDetailPage}/>
      </Switch>
    </div>
  );
}

export default App;
